package com.epam.cisen.core.ui;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * I am available by "/services/status" path.
 *
 */
@Component
@Service(RestExample.class)
@Path("/status")
public class RestExample {

    @GET
    @Produces("text/plain")
    public String getStatus() {
        return "active";
    }
}
