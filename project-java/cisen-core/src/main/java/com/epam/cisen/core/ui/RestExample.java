package com.epam.cisen.core.ui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * I am available by "/services/status" path.
 *
 */
@Component
@Service(RestExample.class)
@Path("/status")
public class RestExample {

    @GET
    @Produces("application/json")
    public SimpleJsonResponse getStatus() {
        return new SimpleJsonResponse("myName", "myValue");
    }
}
