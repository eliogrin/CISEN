package com.epam.cisen.core.api.dto;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class ConfigDTO {

    @Id
    @ObjectId
    private String id;
    private String type;
    private BaseType baseType;
    private String description;


    public ConfigDTO() {

    }

    public ConfigDTO(String type, BaseType baseType) {
        this(type, baseType, "");
    }

    public ConfigDTO(String type, BaseType baseType, String description) {
        this.type = type;
        this.baseType = baseType;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum BaseType {
        CI("ci"),
        MESSENGER("messenger"),
        PROCESSOR("processor");

        String dbName;

        BaseType(String dbName) {
            this.dbName = dbName;
        }

        public String getDbName() {
            return dbName;
        }
    }
}
