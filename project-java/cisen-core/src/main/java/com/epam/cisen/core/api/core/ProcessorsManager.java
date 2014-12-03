package com.epam.cisen.core.api.core;

import com.epam.cisen.core.api.Processor;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.api.MongoDBService;
import org.apache.felix.scr.annotations.*;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.osgi.service.component.ComponentContext;

import java.util.HashMap;
import java.util.Map;

@Component(immediate = true)
@Service(ProcessorsManager.class)
@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = Processor.class, bind = "bindProcessor", unbind = "unbindProcessor")
public class ProcessorsManager {

    private Map<String, Thread> pool = new HashMap<>(6);

    @Activate
    public void activate(ComponentContext componentContext) {
        System.out.println("******** Connector call manager **********");
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
