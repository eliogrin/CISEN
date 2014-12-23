package com.epam.cisen.core.ui;

import com.epam.cisen.core.api.MongoDBService;
import com.epam.cisen.core.api.dto.Constants;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by Vladislav on 22.12.2014.
 */
@Component
@Service(PluginsService.class)
@Path("/plugins")
public class PluginsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginsService.class);
    @Reference
    protected MongoDBService mongoDBService;

    @GET
    @Path("/cis")
    @Produces("application/json")
    public String getCIs() {
        LOGGER.info("Try to take cis configs");
        return get(Constants.DB.CI_PLUGINS);
    }

    @GET
    @Path("/processors")
    @Produces("application/json")
    public String getProcessors() {
        LOGGER.info("Try to take processors configs");
        return get(Constants.DB.PROCESSOR_PLUGINS);
    }

    @GET
    @Path("/messengers")
    @Produces("application/json")
    public String getMessengers() {
        LOGGER.info("Try to take messengers configs");
        return get(Constants.DB.MESSENGERS_PLUGIN);
    }


    private String get(Constants.DB table) {
        List<DBObject> list = mongoDBService.getDBCollection(table).find().toArray();
        return JSON.serialize(list);
    }

}
