package com.epam.cisen.core.ui;

import com.epam.cisen.core.api.MongoDBService;
import com.epam.cisen.core.api.dto.Constants;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.util.UUID;

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

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public String save(String ciStr) {
        BasicDBList jobConfigs = (BasicDBList) JSON.parse(ciStr);
        StringBuilder result = new StringBuilder().append("[");
        result.append(save((BasicDBObject) jobConfigs.get(0), Constants.DB.CI_CONFIGS)).append(",");
        result.append(save((BasicDBObject) jobConfigs.get(1), Constants.DB.MESSENGER_CONFIGS)).append(",");
        result.append(save((BasicDBObject) jobConfigs.get(2), Constants.DB.PROCESSOR_CONFIGS)).append("]");

        return result.toString();
    }

    private String save(BasicDBObject object, Constants.DB table) {
        if (object == null) {
            return "{}";
        }
        object.append("_id", UUID.randomUUID().toString().replaceAll("-", ""));
        mongoDBService.getDBCollection(table).insert(object);
        return object.toString();
    }

    private String get(Constants.DB table, String id) {
        DBObject query = new BasicDBObject("_id", id);
        DBObject result = mongoDBService.getDBCollection(table).findOne(query);
        return JSON.serialize(result);
    }

}
