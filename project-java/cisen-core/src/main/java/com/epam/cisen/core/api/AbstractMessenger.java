package com.epam.cisen.core.api;

import java.io.IOException;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.Constants;
import com.epam.cisen.core.api.dto.ToSend;

/**
 * AbstractMessenger - abstract component class which should be used for creation of new
 * "messenger" type of plugin. Do not create activation method for service.
 * All plugins should register yourself in system, so we use activator as registration point.
 * If you need to add some logic in activator you can use AbstractPlugin.activatePlugin
 * and AbstractPlugin.deactivatePlugin method which call during activation.
 *
 * @param <T>
 */
@Component(componentAbstract = true)
public abstract class AbstractMessenger<T extends ConfigDTO> extends AbstractPlugin<T> implements Messenger {

    @Override
    protected Constants.DB getTemplateTableName() {
        return Constants.DB.MESSENGERS_PLUGIN;
    }

    @Override
    protected Constants.DB getConfigTableName() {
        return Constants.DB.MESSENGER_CONFIGS;
    }

    @Override
    public void send() {
        Map<String, T> jobs = getJobs();
        MongoCollection collection = mongoDBService.getCollection(Constants.DB.TO_SEND);
        for (Map.Entry<String, T> entry : jobs.entrySet()) {
            String toSendQuery = "{jobId:'" + entry.getKey() + "'}";
            try {
                MongoCursor<ToSend> messageCursor = collection.find(toSendQuery).as(ToSend.class);
                for (ToSend message : messageCursor) {
                    send(entry.getValue(), message);
                }
                collection.remove(toSendQuery);
                messageCursor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    protected abstract void send(T configDTO, ToSend message);

}
