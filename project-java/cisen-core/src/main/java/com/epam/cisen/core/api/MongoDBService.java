package com.epam.cisen.core.api;

import com.epam.cisen.core.api.dto.Constants;
import org.jongo.MongoCollection;

public interface MongoDBService {

    MongoCollection getCollection(Constants.DB table);

}
