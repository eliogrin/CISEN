package com.epam.cisen.core.api.core;

import java.util.HashMap;
import java.util.Map;

import com.epam.cisen.core.api.util.Log;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.osgi.service.component.ComponentContext;

import com.epam.cisen.core.api.Connector;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.api.MongoDBService;

@Component(immediate = true)
@Service(ConnectorManager.class)
@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = Connector.class, bind = "bindConnector", unbind = "unbindConnector")
public class ConnectorManager {

    private Map<String, Thread> pool = new HashMap<>(6);

    @Activate
    public void activate(ComponentContext componentContext) {
        Log.info("Activate Connector");
        System.out.println("******** Connector call manager **********");
    }

    protected void bindConnector(Connector connector) {
        final Thread thread = new Thread(new ConnectorCaller(connector));
        thread.start();
        pool.put(connector.getClass().getCanonicalName(), thread);
    }

    protected void unbindConnector(Connector connector) {
        final Thread thread = pool.get(connector.getClass().getCanonicalName());
        thread.interrupt();
    }

    private static class ConnectorCaller implements Runnable {

        private Connector messenger;

        ConnectorCaller(Connector messenger) {
            // System.out.println("******** ConnectorCaller **********");
            this.messenger = messenger;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    messenger.check();
                    Thread.sleep(20000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
