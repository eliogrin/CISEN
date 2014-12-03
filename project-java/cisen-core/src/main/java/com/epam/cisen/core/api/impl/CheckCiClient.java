package com.epam.cisen.core.api.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

@Component
@Service(CheckCiClient.class)
public class CheckCiClient {

    @Reference
    private volatile CheckCiJob service;

    @Activate
    public void activate() {

        service.runJob("Hello from another component");
    }
}
