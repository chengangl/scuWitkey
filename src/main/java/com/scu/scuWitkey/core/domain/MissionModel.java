package com.scu.scuWitkey.core.domain;

import java.io.Serializable;
import java.util.Date;

public class MissionModel implements Serializable {

    private static final long serialVersionUID = -6595686941298002012L;
    private long id;
    private String missionTitle;
    private String missionDescription;
    private Date missionBeginDate;
    private Date missionEndDate;
    private String missionWinningMode;
    private String missionMode;
    private int missionReward;
    private long missionPublisherId;
    private String missionStatus;
    private String missionData;
    private String missionDataName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMissionTitle() {
        return missionTitle;
    }

    public void setMissionTitle(String missionTitle) {
        this.missionTitle = missionTitle;
    }

    public String getMissionDescription() {
        return missionDescription;
    }

    public void setMissionDescription(String missionDescription) {
        this.missionDescription = missionDescription;
    }

    public Date getMissionBeginDate() {
        return missionBeginDate;
    }

    public void setMissionBeginDate(Date missionBeginDate) {
        this.missionBeginDate = missionBeginDate;
    }

    public Date getMissionEndDate() {
        return missionEndDate;
    }

    public void setMissionEndDate(Date missionEndDate) {
        this.missionEndDate = missionEndDate;
    }

    public String getMissionWinningMode() {
        return missionWinningMode;
    }

    public void setMissionWinningMode(String missionWinningMode) {
        this.missionWinningMode = missionWinningMode;
    }

    public String getMissionMode() {
        return missionMode;
    }

    public void setMissionMode(String missionMode) {
        this.missionMode = missionMode;
    }

    public int getMissionReward() {
        return missionReward;
    }

    public void setMissionReward(int missionReward) {
        this.missionReward = missionReward;
    }

    public long getMissionPublisherId() {
        return missionPublisherId;
    }

    public void setMissionPublisherId(long missionPublisherId) {
        this.missionPublisherId = missionPublisherId;
    }

    public String getMissionStatus() {
        return missionStatus;
    }

    public void setMissionStatus(String missionStatus) {
        this.missionStatus = missionStatus;
    }

    public String getMissionData() {
        return missionData;
    }

    public void setMissionData(String missionData) {
        this.missionData = missionData;
    }

    public String getMissionDataName() {
        return missionDataName;
    }

    public void setMissionDataName(String missionDataName) {
        this.missionDataName = missionDataName;
    }
}
