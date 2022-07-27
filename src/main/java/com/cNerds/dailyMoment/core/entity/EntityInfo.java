package com.cNerds.dailyMoment.core.entity;

import java.util.Date;

import com.cNerds.dailyMoment.user.UserInfo;

public interface EntityInfo {
    public Date getCreateOn();
    public void setCreateOn(Date createOn);
    public UserInfo getCreator();
    public void setCreator(UserInfo creator);
    public Date getModifiedOn();
    public void setModifiedOn(Date modifiedOn);
    public UserInfo getModifier();
    public void setModifier(UserInfo modifier);
}