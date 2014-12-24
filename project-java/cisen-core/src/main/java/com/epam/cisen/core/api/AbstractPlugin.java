package com.epam.cisen.core.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.cisen.core.api.dto.CiJob;
import com.epam.cisen.core.api.dto.CiUser;
import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.ConfigDTO.BaseType;
import com.epam.cisen.core.api.dto.Constants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Component(componentAbstract = true)
public abstract class AbstractPlugin<T extends ConfigDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPlugin.class);

    private T configTemplate;

    @Reference
    protected MongoDBService mongoDBService;

    protected abstract T getPluginTemplateConfig();

    private List<T> testData;

    protected List<T> getTestData() {
        return null;
    }

    /**
     * In case, you need do some action during service activation use this method.
     *
     * @param componentContext {@link ComponentContext}
     */
    protected abstract void activatePlugin(ComponentContext componentContext);

    /**
     * In case, you need do some action during service deactivation.
     */
    protected void deactivatePlugin() {
        //nothing to do
    }

    protected abstract Constants.DB getTemplateTableName();

    protected abstract Constants.DB getConfigTableName();

    /**
     * @return active configs set for selected plugin
     */
    protected Set<T> getPluginConfigs() {
        HashSet<T> configsByType = Sets.newHashSet();
        MongoCollection collection = mongoDBService.getCollection(getConfigTableName());
        try {
            MongoCursor<? extends ConfigDTO> mongoCursor = collection.find("{type:'" + configTemplate.getType() + "'}")
                    .as(configTemplate.getClass());

            for (ConfigDTO configDTO : mongoCursor) {
                configsByType.add((T) configDTO);
            }
            mongoCursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configsByType;
    }

    protected Map<String, T> getJobs(Set<T> configs) {
        Map<String, T> jobsByIds = Maps.newHashMap();

        try {
            MongoCollection collection = mongoDBService.getCollection(Constants.DB.USERS);
            for (T config : configs) {
                String query = String.format("{ jobs:{$elemMatch:{%s: '%s'} } }", config.getBaseType().getDbName(),
                        config.getId());
                List<CiJob> jobs = collection.distinct("jobs").query(query).as(CiJob.class);
                for (CiJob job : jobs) {
                    jobsByIds.put(job.getId(), config);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobsByIds;
    }

    protected Map<String, T> getJobs() {
        return getJobs(getPluginConfigs());
    }

    @Activate
    public void activate(ComponentContext componentContext) {
        registerPlugin();
        activatePlugin(componentContext);
    }

    @Deactivate
    public void deactivate() {
        unregisterPlugin();
    }

    protected MongoCollection getCollection() {
        return mongoDBService.getCollection(getTemplateTableName());
    }

    /**
     * Use for setup test data only.
     */
    private void updateUser(String userName, String jobName, T info) throws Exception {
        MongoCollection collection = mongoDBService.getCollection(Constants.DB.USERS);
        MongoCursor<CiUser> users = collection.find(String.format("{name:'%s'}", userName)).as(CiUser.class);

        CiUser ciUser;
        if (users.hasNext()) {
            ciUser = users.next();
        } else {
            ciUser = new CiUser();
            ciUser.setName(userName);
            collection.save(ciUser);
        }
        users.close();
        if (ciUser.getJobs() == null || ciUser.getJobs().isEmpty()) {
            ciUser.setJobs(new ArrayList<CiJob>());
            CiJob job = new CiJob();
            job.setName(jobName);
            ciUser.getJobs().add(job);
        }
        CiJob job = ciUser.getJobs().get(0);

        if (BaseType.CI.equals(info.getBaseType())) {
            job.setCi(info.getId());
        } else if (BaseType.MESSENGER.equals(info.getBaseType())) {
            job.setMessenger(info.getId());
        } else if (BaseType.PROCESSOR.equals(info.getBaseType())) {
            job.setProcessor(info.getId());
        }
        collection.update("{name: '" + userName + "'}").with(ciUser);
    }

    protected void registerPlugin() {
        try {
            testData = getTestData();

            if (Constants.DEBUG && testData != null && testData.size() > 0) {
                MongoCollection testCollection = mongoDBService.getCollection(getConfigTableName());
                testCollection.insert(testData.toArray());

                updateUser("Luke", "TeamCity CISEN mail notifications", testData.get(0));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        configTemplate = getPluginTemplateConfig();
        if (configTemplate == null) {
            throw new IllegalArgumentException("Provide a valid plugin info");
        }
        try {
            final String type = configTemplate.getType();
            LOGGER.info("Try to register plugin [{}]", type);

            final MongoCollection collection = getCollection();

            try (MongoCursor previousConfig = collection.find(String.format("{type: '%s'}", type)).as(
                    configTemplate.getClass())) {
                if (previousConfig.hasNext()) {
                    LOGGER.info("Plugin [{}] already registered!", type);
                    return;
                }
            }

            LOGGER.info("Plugin not register yet. Register plugin with [{}] type.", type);
            collection.save(configTemplate);
            LOGGER.info("Plugin [{}] was registered successfully.", type);
        } catch (IOException ex) {
            LOGGER.error("Fail to close cursor ", ex);
        }
    }

    protected void unregisterPlugin() {
        MongoCollection collection = getCollection();
        final String type = configTemplate.getType();
        LOGGER.info("Try to unregister plugin [{}]", type);
        collection.remove(String.format("{type: '%s'}", type));
        LOGGER.info("Plugin [{}] was removed successfully.", type);

        if (Constants.DEBUG && testData != null && testData.size() > 0) {
            MongoCollection testCollection = mongoDBService.getCollection(getConfigTableName());
            for (T item : testData) {
                String query = String.format("{id : '%s'}", item.getId());
                testCollection.remove(query);
            }
        }
    }

}
