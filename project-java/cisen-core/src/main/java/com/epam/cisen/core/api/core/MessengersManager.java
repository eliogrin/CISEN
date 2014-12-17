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

import com.epam.cisen.core.api.Messenger;

@Component(immediate = true)
@Service(MessengersManager.class)
@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = Messenger.class, bind = "bindMessenger", unbind = "unbindMessenger")
public class MessengersManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessengersManager.class);

    private Map<String, Thread> pool = new HashMap<>(6);

    @Activate
    public void activate(ComponentContext componentContext) {
        LOGGER.info("Activate Messenger Mapper");
    }

    protected void bindMessenger(Messenger ciMessenger) {
        final Thread thread = new Thread(new MessageCaller(ciMessenger));
        thread.start();
        pool.put(ciMessenger.getClass().getCanonicalName(), thread);
    }

    protected void unbindMessenger(Messenger ciMessenger) {
        final Thread thread = pool.get(ciMessenger.getClass().getCanonicalName());
        thread.interrupt();
    }

    private static class MessageCaller implements Runnable {

        private Messenger messenger;

        MessageCaller(Messenger messenger) {
            this.messenger = messenger;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    messenger.send();
                    Thread.sleep(10000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
