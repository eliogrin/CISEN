package com.epam.cisen.core.api.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
@Service(CheckCiJob.class)
public class CheckCiJob {

    ExecutorService exec = Executors.newFixedThreadPool(4);

    public void runJob(String message) {
        exec.execute(new ConnectionRunnable(message));
    }

    private static class ConnectionRunnable implements Runnable {

        private String message;

        ConnectionRunnable(String message) {
            this.message = message;

        }

        public void run() {

            try {
                while (true) {
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
