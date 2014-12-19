package com.epam.cisen.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    public static final String ALIAS = "/";
    public static final String RESOURCE_FOLDER = "/webapp";

    private ServiceTracker httpServiceTracker;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        LOGGER.info("Cisen Core started");

        httpServiceTracker = getResourceServiceTracker(bundleContext);
        httpServiceTracker.open();

    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        LOGGER.info("Cisen Core stopped");

        httpServiceTracker.close();
    }

    @SuppressWarnings("unchecked")
    private ServiceTracker getResourceServiceTracker(final BundleContext bundleContext) {

        return new ServiceTracker(bundleContext, HttpService.class.getName(), null) {
            public void removedService(ServiceReference reference, Object service) {
                LOGGER.info("HTTP service is no longer available, unregister our resources...");
                try {
                    ((HttpService) service).unregister(ALIAS);
                } catch (IllegalArgumentException ex) {
                    LOGGER.error("Ignore; servlet registration probably failed earlier on...");
                }
            }

            public Object addingService(ServiceReference reference) {
                LOGGER.info("HTTP service is available, register our resources...");
                HttpService httpService = (HttpService) this.context.getService(reference);
                try {
                    httpService.registerResources(ALIAS, RESOURCE_FOLDER, null);
                } catch (Exception ex) {
                    LOGGER.info("Fail to register resources...", ex);
                }
                return httpService;
            }
        };
    }

}
