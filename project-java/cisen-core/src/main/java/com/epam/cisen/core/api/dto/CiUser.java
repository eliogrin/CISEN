package com.epam.cisen.core.api.dto;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

import java.util.List;

public class CiUser {


    @Id
    @ObjectId
    private String id;
    private String name;
    private String email;
    private String skype;
    private String mobile;
    private List<CiJob> jobs;

    public CiUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<CiJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<CiJob> jobs) {
        this.jobs = jobs;
    }
}
