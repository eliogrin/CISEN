package com.epam.cisen.core.api;

import com.epam.cisen.core.api.dto.CiJob;
import com.epam.cisen.core.api.dto.CiUser;
import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.ConfigDTO.BaseType;
import com.epam.cisen.core.api.dto.Constants;
import com.epam.cisen.core.api.util.Log;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.io.IOException;
import java.util.*;

/**
 * Created by Vladislav on 28.11.2014.
 */
@Component(componentAbstract = true)
public abstract class AbstractPlugin<T extends ConfigDTO> {

    private T configTemplate;

    @Reference
    protected MongoDBService mongoDBService;

    protected abstract T getPluginTemplateConfig();

    private List<T> testData;

    protected List<T> getTestData() {
        return new ArrayList<>();
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
                String query = String.format("{ jobs:{$elemMatch:{%s: '%s'} } }",
                        config.getBaseType().getDbName(), config.getId());
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
    public void activate() {
        registerPlugin();
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
        CiUser ciUser = null;
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

            if (Constants.DEBUG
                    && testData != null && testData.size() > 0) {
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
            Log.info("Try to register plugin [%s]", type);

            final MongoCollection collection = getCollection();
            final MongoCursor previousConfig = collection.find(String.format("{type: '%s'}", type)).as(
                    configTemplate.getClass());
            try {
                if (previousConfig.hasNext()) {
                    Log.info("Plugin [%s] already registered!", type);
                    return;
                }
            } finally {
                previousConfig.close();
            }

            Log.info("Plugin not register yet . Register plugin with [%s] type.", type);
            collection.save(configTemplate);
            Log.info("Plugin [%s] was registered successfully.", type);
        } catch (IOException ex) {
            Log.error("Fail to close cursor " + ex.getMessage());
        }
    }

    protected void unregisterPlugin() {
        MongoCollection collection = getCollection();
        final String type = configTemplate.getType();
        Log.info("Try to unregister plugin [%s]", type);
        collection.remove(String.format("{type: '%s'}", type));
        Log.info("Plugin [%s] was removed successfully.", type);


        if (Constants.DEBUG
                && testData != null && testData.size() > 0) {
            MongoCollection testCollection = mongoDBService.getCollection(getConfigTableName());
            for (T item : testData) {
                String query = String.format("{id : '%s'}", item.getId());
                testCollection.remove(query);
            }
        }
    }

}
