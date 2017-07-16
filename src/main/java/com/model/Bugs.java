package com.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "bugs")
public class Bugs {
    @Id
    private String id;

    @Field
    private String bugTitle;
    @Field
    private String bugCreationDate;
    @Field
    private String bugRaisedByEmail;
    @Field
    private String bugDescription;
    @Field
    private String status;

    public String getBugCreationDate() {
        return bugCreationDate;
    }

    public void setBugCreationDate(String bugCreationDate) {
        this.bugCreationDate = bugCreationDate;
    }

    public String getBugRaisedByEmail() {
        return bugRaisedByEmail;
    }

    public void setBugRaisedByEmail(String bugRaisedByEmail) {
        this.bugRaisedByEmail = bugRaisedByEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBugTitle() {
        return bugTitle;
    }

    public void setBugTitle(String bugTitle) {
        this.bugTitle = bugTitle;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
