package com.epam.cisen.core.api.impl;

import com.epam.cisen.core.api.MongoDBService;
import com.epam.cisen.core.api.dto.Constants;
import com.epam.cisen.core.api.util.Log;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.jongo.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class MongoDBServiceImpl implements MongoDBService {

    private Mongo m_mongo;
    private DB m_db;
    private Jongo jongo;
    private Map<String, MongoCollection> collections = new HashMap<>();

    @Activate
    public void start() {
        Log.info("Starting MongoDb service...");
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient(Constants.HOST, Constants.PORT);
            this.m_db = mongoClient.getDB(Constants.DB_NAME);
            this.m_mongo = m_db.getMongo();
            this.jongo = new Jongo(m_db);
            setUpDB();
            Log.info("MongoDb service was started successfully.");
        } catch (UnknownHostException e) {
            Log.error("Fail to connect: " + e.getMessage());
        }
    }

    private void setUpDB() {
        Log.info("Setting up DB...");

        MongoCollection indexes = jongo.getCollection("system.indexes");
        for (Constants.DB table : Constants.DB.values()) {
            MongoCollection collection = getCollection(table);
            Log.info("Find indexes for table [%s].", table.getTable());
            for (String index : table.getIndexes()) {
                Log.debug("Try to find index [%s] for table [%s].", index, table.getTable());
                if (indexes.findOne("{key : # } ", index).as(Object.class) == null) {
                    Log.debug("Cannot find index [%s] for table [%s].", index, table.getTable());
                    Log.debug("Try to create index [%s] for table [%s].", index, table.getTable());
                    collection.ensureIndex(index);
                    Log.debug("Index [%s] for table [%s] was created.", index, table.getTable());
                }
            }
        }

        Log.info("DB was set up.");
    }

    @Override
    public MongoCollection getCollection(Constants.DB table) {
        Log.debug("Try to get [%s] collection", table.getTable());
        MongoCollection result = collections.get(table.getTable());
        if (result == null) {
            Log.debug("Cannot find collection [%s], Try to create new connection.", table.getTable());
            result = jongo.getCollection(table.getTable());
            collections.put(table.getTable(), result);
            Log.debug("Connection to [%s] was created.", table.getTable());
        }
        return result;
    }

    @Deactivate
    public void close() {
        Log.info("Closing MongoDb connection...");
        m_mongo.close();
        Log.info("MongoDb connection was stopped successfully.");
    }
}
