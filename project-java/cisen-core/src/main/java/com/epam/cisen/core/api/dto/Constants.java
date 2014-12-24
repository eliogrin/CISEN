package com.epam.cisen.core.api.dto;

public class Constants {

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
