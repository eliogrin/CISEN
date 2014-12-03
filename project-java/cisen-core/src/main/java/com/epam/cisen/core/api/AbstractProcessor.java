package com.epam.cisen.core.api;

import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.Constants;
import com.epam.cisen.core.api.dto.ToSend;
import org.apache.felix.scr.annotations.Component;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

@Component(componentAbstract = true)
public abstract class AbstractProcessor<T extends ConfigDTO> extends AbstractPlugin<T> implements Processor {


    @Override
    protected Constants.DB getTemplateTableName() {
        return Constants.DB.PROCESSOR_PLUGINS;
    }

    @Override
    protected Constants.DB getConfigTableName() {
        return Constants.DB.PROCESSOR_CONFIGS;
    }

    protected void putMessage(String jobId, String subject, String body) {
        MongoCollection collection = mongoDBService.getCollection(Constants.DB.TO_SEND);
        ToSend message = new ToSend();
        message.setJobId(jobId);
        message.setSubject(subject);
        message.setBody(body);
        collection.save(message);
    }
}
