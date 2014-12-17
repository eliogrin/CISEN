package com.epam.cisen.core.api.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.cisen.core.api.Processor;

@Component(immediate = true)
@Service(ProcessorsManager.class)
@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = Processor.class, bind = "bindProcessor", unbind = "unbindProcessor")
public class ProcessorsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorsManager.class);

    private Map<String, Thread> pool = new HashMap<>(6);

    @Activate
    public void activate(ComponentContext componentContext) {
        LOGGER.info("Activate Processor Mapper");
    }

    protected void bindProcessor(Processor processor) {
        final Thread thread = new Thread(new ProcessorCaller(processor));
        thread.start();
        pool.put(processor.getClass().getCanonicalName(), thread);
    }

    protected void unbindProcessor(Processor processor) {
        final Thread thread = pool.get(processor.getClass().getCanonicalName());
        thread.interrupt();
    }

    private static class ProcessorCaller implements Runnable {

        private Processor processor;

        ProcessorCaller(Processor processor) {
            this.processor = processor;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    processor.process();
                    Thread.sleep(20000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
