package com.epam.cisen.core.api.impl;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.cisen.core.api.MongoDBService;
import com.epam.cisen.core.api.dto.Constants;
import com.epam.cisen.core.api.util.PropertiesUtil;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Component(label = "MongoDB connector", metatype = true)
@Service
@Properties({ @Property(label = "Host", value = "127.0.0.1", name = MongoDBServiceImpl.MONGO_HOST),
        @Property(label = "Port", intValue = 27017, name = MongoDBServiceImpl.MONGO_PORT),
        @Property(label = "DB name", value = "cisen", name = MongoDBServiceImpl.MONGO_DB_NAME) })
public class MongoDBServiceImpl implements MongoDBService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBServiceImpl.class);

    static final String MONGO_HOST = "mongo.host";
    static final String MONGO_PORT = "mongo.port";
    static final String MONGO_DB_NAME = "mongo.db.name";

    private Mongo m_mongo;
    private Jongo jongo;
    private Map<String, MongoCollection> collections = new HashMap<>();

    @Activate
    public void start(Map<String, Object> properties) {
        LOGGER.info("Starting MongoDb service...");
        MongoClient mongoClient;
        try {
            String host = PropertiesUtil.toString(properties.get(MONGO_HOST), "127.0.0.1");
            int port = PropertiesUtil.toInteger(properties.get(MONGO_PORT), 27017);
            String dbName = PropertiesUtil.toString(properties.get(MONGO_DB_NAME), "cisen");

            mongoClient = new MongoClient(host, port);
            DB mDb = mongoClient.getDB(dbName);

            this.m_mongo = mDb.getMongo();
            this.jongo = new Jongo(mDb);
            setUpDB();
            LOGGER.info("MongoDb service was started successfully.");
        } catch (UnknownHostException e) {
            LOGGER.error("Fail to connect: ", e);
        }
    }

    private void setUpDB() {
        LOGGER.info("Setting up DB...");

        MongoCollection indexes = jongo.getCollection("system.indexes");
        for (Constants.DB table : Constants.DB.values()) {
            MongoCollection collection = getCollection(table);
            LOGGER.info("Find indexes for table [{}].", table.getTable());
            for (String index : table.getIndexes()) {
                LOGGER.debug("Try to find index [{}] for table [{}].", index, table.getTable());
                if (indexes.findOne("{key : # } ", index).as(Object.class) == null) {
                    LOGGER.debug("Cannot find index [{}] for table [{}].", index, table.getTable());
                    LOGGER.debug("Try to create index [{}] for table [{}].", index, table.getTable());
                    collection.ensureIndex(index);
                    LOGGER.debug("Index [{}] for table [{}] was created.", index, table.getTable());
                }
            }
        }

        LOGGER.info("DB was set up.");
    }

    @Override
    public MongoCollection getCollection(Constants.DB table) {
        LOGGER.debug("Try to get [{}] collection", table.getTable());
        MongoCollection result = collections.get(table.getTable());
        if (result == null) {
            LOGGER.debug("Cannot find collection [{}], Try to create new connection.", table.getTable());
            result = jongo.getCollection(table.getTable());
            collections.put(table.getTable(), result);
            LOGGER.debug("Connection to [{}] was created.", table.getTable());
        }
        return result;
    }

    @Override
    public DBCollection getDBCollection(Constants.DB table) {
        LOGGER.debug("Try to get [{}] db collection", table.getTable());
        return getCollection(table).getDBCollection();
    }

    @Deactivate
    public void close() {
        LOGGER.info("Closing MongoDb connection...");
        m_mongo.close();
        LOGGER.info("MongoDb connection was stopped successfully.");
    }
}
