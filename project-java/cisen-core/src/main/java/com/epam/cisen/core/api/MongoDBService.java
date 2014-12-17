package com.epam.cisen.core.api;

import com.epam.cisen.core.api.dto.Constants;
import com.mongodb.DBCollection;
import org.jongo.MongoCollection;

public interface MongoDBService {

    MongoCollection getCollection(Constants.DB table);

    DBCollection getDBCollection(Constants.DB table);
}
