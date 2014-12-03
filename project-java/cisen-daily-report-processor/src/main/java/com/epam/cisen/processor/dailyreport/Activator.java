package com.epam.cisen.processor.dailyreport;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Report provider started [daily processor]");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Report provider stopped [daily processor]");
    }
}
