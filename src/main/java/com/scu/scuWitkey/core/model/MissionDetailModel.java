package com.scu.scuWitkey.core.model;

import com.scu.scuWitkey.core.domain.MissionModel;
import com.scu.scuWitkey.core.domain.MissionUserRelationshipModel;
import com.scu.scuWitkey.core.domain.UserModel;
import java.io.Serializable;
import java.util.Date;

public class MissionDetailModel implements Serializable {
    private static final long serialVersionUID = -3465085715281292809L;

    private MissionModel missionModel;
    private UserModel missionPublisher;
    private MissionUserRelationshipModel missionUserRelationshipModel;

    public MissionModel getMissionModel() {
        return missionModel;
    }

    public void setMissionModel(MissionModel missionModel) {
        this.missionModel = missionModel;
    }

    public UserModel getMissionPublisher() {
        return missionPublisher;
    }

    public void setMissionPublisher(UserModel missionPublisher) {
        this.missionPublisher = missionPublisher;
    }

    public MissionUserRelationshipModel getMissionUserRelationshipModel() {
        return missionUserRelationshipModel;
    }

    public void setMissionUserRelationshipModel(MissionUserRelationshipModel missionUserRelationshipModel) {
        this.missionUserRelationshipModel = missionUserRelationshipModel;
    }
}
