package com.scu.scuWitkey.core.service;

import com.scu.scuWitkey.core.domain.MissionUserRelationshipModel;
import com.scu.scuWitkey.core.mapper.MissionUserRelationshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MissionUserRelationshipService {
    @Autowired
    private MissionUserRelationshipMapper missionUserRelationshipMapper;

    public void insertMissionUserRelationship(MissionUserRelationshipModel missionUserRelationshipModel) {
        this.missionUserRelationshipMapper.insertMissionUserRelationship(missionUserRelationshipModel);
    }

    public void updateMissionUserRelationship(long id, String missionSubmitData, String missionSubmitDataName, Date missionFinishDate) {
        this.missionUserRelationshipMapper.updateMissionUserRelationship(id, missionSubmitData, missionSubmitDataName, missionFinishDate);
    }

    public void bidMissionUserRelationship(long id) {
        this.missionUserRelationshipMapper.bidMissionUserRelationship(id);
    }

    public List<MissionUserRelationshipModel> getMissionUserRelationshipByUserId(long userId) {
        return this.missionUserRelationshipMapper.getMissionUserRelationshipByUserId(userId);
    }

    public List<MissionUserRelationshipModel> getMissionUserRelationshipByMissionId(long missionId) {
        return this.missionUserRelationshipMapper.getMissionUserRelationshipByMissionId(missionId);
    }
}
