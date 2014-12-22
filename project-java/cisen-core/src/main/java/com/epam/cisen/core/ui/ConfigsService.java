package com.epam.cisen.core.ui;

import com.epam.cisen.core.api.MongoDBService;
import com.epam.cisen.core.api.dto.Constants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by Vladislav on 22.12.2014.
 */
@Component
@Service(ConfigsService.class)
@Path("/config")
public class ConfigsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigsService.class);

    @Reference
    protected MongoDBService mongoDBService;

    @GET
    @Path("/ci/{id}")
    @Produces("application/json")
    public String getCI(@PathParam("id") String id) {
        LOGGER.info("Try to find ci config with id = [{}]", id);
        return get(Constants.DB.CI_CONFIGS, id);
    }

    @GET
    @Path("/processor/{id}")
    @Produces("application/json")
    public String getProcessor(@PathParam("id") String id) {
        LOGGER.info("Try to find processor config with id = [{}]", id);
        return get(Constants.DB.PROCESSOR_CONFIGS, id);
    }

    @GET
    @Path("/messenger/{id}")
    @Produces("application/json")
    public String getMessenger(@PathParam("id") String id) {
        LOGGER.info("Try to find messenger config with id = [{}]", id);
        return get(Constants.DB.MESSENGER_CONFIGS, id);
    }

    private String get(Constants.DB table, String id) {
        DBObject query = new BasicDBObject("_id", new ObjectId(id));
        DBObject result = mongoDBService.getDBCollection(table).findOne(query);
        return JSON.serialize(result);
    }

}
