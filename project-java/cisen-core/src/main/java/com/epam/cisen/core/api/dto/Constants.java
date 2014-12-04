package com.epam.cisen.core.api.dto;

/**
 * Created by Vladislav on 28.11.2014.
 */
public class Constants {

    //DB credentials
    public static final String HOST = "localhost";
    public static final int PORT = 27017;
    public static final String DB_NAME = "cisen";
    //
    public static final boolean DEBUG = true;

    public static enum DB {
        //Plugin templates
        CI_PLUGINS("plugins_ci"),
        PROCESSOR_PLUGINS("plugins_processors"),
        MESSENGERS_PLUGIN("plugins_messengers"),
        //Plugin settings
        CI_CONFIGS("configs_ci"),
        PROCESSOR_CONFIGS("configs_processor"),
        MESSENGER_CONFIGS("configs_messenger"),
        //Users with jobs
        USERS("users",
                "{jobs.ci:1}",
                "{jobs.messenger:1}",
                "{jobs.processor:1}"),
        //
        TO_SEND("to_send"),
        //
        BUILDS("builds");

        private String tableName;
        private String[] indexes;

        DB(String tableName, String... indexes) {
            this.tableName = tableName;
            this.indexes = indexes;
        }

        public String getTable() {
            return tableName;
        }

        public String[] getIndexes() {
            return indexes;
        }
    }
}
