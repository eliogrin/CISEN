package com.epam.cisen.skype;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        LOGGER.info("Skype plugin started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        LOGGER.info("Skype plugin stopped");
    }
}
