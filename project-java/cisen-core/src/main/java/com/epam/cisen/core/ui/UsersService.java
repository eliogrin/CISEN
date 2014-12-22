package com.epam.cisen.core.ui;

import com.epam.cisen.core.api.MongoDBService;
import com.epam.cisen.core.api.dto.CiUser;
import com.epam.cisen.core.api.dto.Constants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by Vladislav on 22.12.2014.
 */
@Component
@Service(UsersService.class)
@Path("/users")
public class UsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);

    @Reference
    protected MongoDBService mongoDBService;

    @GET
    @Produces("application/json")
    public CiUser getUser(@QueryParam("name") String name) {
        LOGGER.info("Try to find user with name = {}", name);
        CiUser result = mongoDBService.getCollection(Constants.DB.USERS).findOne("{name:#}", name).as(CiUser.class);
        if (result == null) {
            LOGGER.error("Cannot find user with name = {}", name);
        }
        return result;
    }

    @POST
    @Consumes("application/json")
    public void create(CiUser body) {
        LOGGER.info("Try to create user [{}]", body.toString());
        mongoDBService.getCollection(Constants.DB.USERS).insert(body);
    }

    @PUT
    @Consumes("application/json")
    public void update(CiUser body) {
        LOGGER.info("Try to create user [{}]", body.toString());
        mongoDBService.getCollection(Constants.DB.USERS).update(new ObjectId(body.getId())).with(body);
    }
}
