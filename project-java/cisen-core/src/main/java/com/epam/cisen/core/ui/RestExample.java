package com.epam.cisen.core.ui;

import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;

import com.epam.cisen.core.api.MongoDBService;
import com.epam.cisen.core.api.dto.Constants;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;


/**
 * I am available by "/services/status" path.
 */
@Component
@Service(RestExample.class)
@Path("/status")
public class RestExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExample.class);

    @Reference
    protected MongoDBService mongoDBService;


    @GET
    @Produces("application/json")
    public SimpleJsonResponse getStatus() {
        return new SimpleJsonResponse("myName", "myValue");
    }

    @GET
    @Path("/test")
    @Produces("application/json")
    public String getDBObject() {
        DBCursor cursor = null;
        try {
            cursor = mongoDBService.getDBCollection(Constants.DB.CI_PLUGINS).find();
            StringBuilder result = new StringBuilder();
            for (DBObject object : cursor.toArray()) {
                result.append(object.toString());
            }
            return result.toString();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @POST
    @Path("/job")
    @Consumes("application/x-www-form-urlencoded")
    public void set(MultivaluedMap<String, String> formParams) {
        LOGGER.info("Params: {}", formParams);
    }
}
