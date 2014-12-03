package com.epam.cisen.skype;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Skype plugin started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Skype plugin stopped");
    }
}
