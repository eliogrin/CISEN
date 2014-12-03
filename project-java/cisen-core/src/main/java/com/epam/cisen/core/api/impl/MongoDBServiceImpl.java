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
            Log.info("MongoDb service was started successfully.");
        } catch (UnknownHostException e) {
            Log.error("Fail to connect: " + e.getMessage());
        }
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
