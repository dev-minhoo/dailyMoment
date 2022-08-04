package com.cNerds.dailyMoment.core.entity;

import java.util.Date;

import com.cNerds.dailyMoment.user.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class EntityInfoImpl implements EntityInfo {

    private Date createOn;
    @JsonIgnore private UserInfo creator;

    private Date modifiedOn;
    @JsonIgnore private UserInfo modifier;
    
    public Date getCreateOn() {
        return createOn;
    }
    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }
    public UserInfo getCreator() {
        if( creator == null ) creator = new UserInfo();
        return creator;
    }
    public void setCreator(UserInfo creator) {
        this.creator = creator;
    }
    public Date getModifiedOn() {
        return modifiedOn;
    }
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
    public UserInfo getModifier() {
        if( modifier == null ) modifier = new UserInfo();
        return modifier;
    }
    public void setModifier(UserInfo modifier) {
        this.modifier = modifier;
    }
}