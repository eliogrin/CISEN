package com.epam.cisen.core.api.dto;

import org.apache.felix.scr.annotations.Component;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

@Component(inherit = true)
public class ConfigDTO {

    @Id
    @ObjectId
    private String id;
    private String type;
    private BaseType baseType;

    public ConfigDTO() {

    }

    public ConfigDTO(String type, BaseType baseType) {
        this.type = type;
        this.baseType = baseType;
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

    public void setType(String type) {
        this.type = type;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public enum BaseType {
        CI("ci"),
        MESSENGER("messenger"),
        PROCESSOR("processor");

        String dbName;

        BaseType(String dbName){
            this.dbName = dbName;
        }

        public String getDbName() {
            return dbName;
        }
    }
}
