package com.scu.scuWitkey.core.domain;

import java.io.Serializable;
import java.util.Date;

public class MissionUserRelationshipModel implements Serializable{
    private static final long serialVersionUID = -4223517238591767385L;
    private long id;
    private long missionId;
    private long userId;
    private Date missionAcceptDate;
    private Date missionFinishDate;
    private String missionCompletionStatus; //进行中-待审核-已结束
    private String missionSubmitData;
    private String missionSubmitDataName;
    private String missionBidStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMissionId() {
        return missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getMissionAcceptDate() {
        return missionAcceptDate;
    }

    public void setMissionAcceptDate(Date missionAcceptDate) {
        this.missionAcceptDate = missionAcceptDate;
    }

    public Date getMissionFinishDate() {
        return missionFinishDate;
    }

    public void setMissionFinishDate(Date missionFinishDate) {
        this.missionFinishDate = missionFinishDate;
    }

    public String getMissionCompletionStatus() {
        return missionCompletionStatus;
    }

    public void setMissionCompletionStatus(String missionCompletionStatus) {
        this.missionCompletionStatus = missionCompletionStatus;
    }

    public String getMissionSubmitData() {
        return missionSubmitData;
    }

    public void setMissionSubmitData(String missionSubmitData) {
        this.missionSubmitData = missionSubmitData;
    }

    public String getMissionSubmitDataName() {
        return missionSubmitDataName;
    }

    public void setMissionSubmitDataName(String missionSubmitDataName) {
        this.missionSubmitDataName = missionSubmitDataName;
    }

    public String getMissionBidStatus() {
        return missionBidStatus;
    }

    public void setMissionBidStatus(String missionBidStatus) {
        this.missionBidStatus = missionBidStatus;
    }
}
