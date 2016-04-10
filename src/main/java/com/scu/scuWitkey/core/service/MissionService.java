package com.scu.scuWitkey.core.service;

import com.scu.scuWitkey.core.domain.MissionModel;
import com.scu.scuWitkey.core.domain.MissionUserRelationshipModel;
import com.scu.scuWitkey.core.domain.UserModel;
import com.scu.scuWitkey.core.mapper.MissionMapper;
import com.scu.scuWitkey.core.mapper.MissionUserRelationshipMapper;
import com.scu.scuWitkey.core.mapper.UserMapper;
import com.scu.scuWitkey.core.model.MissionDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionService {
    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private MissionUserRelationshipMapper missionUserRelationshipMapper;

    @Autowired
    private UserMapper userMapper;

    public void insertMission(MissionModel missionModel) {
        this.missionMapper.insertMission(missionModel);
    }

    public void updateMission(MissionModel missionModel) {
        this.missionMapper.updateMission(missionModel);
    }

    public void finishMission(long id) {
        this.missionMapper.finishMission(id);
    }

    public void updateMissionStatus() {
        this.missionMapper.updateMissionStatusPending();
        this.missionMapper.updateMissionStatusFinished();
    }

    public List<MissionModel> getMissionByMissionPublisherId(long missionPublisherId, String missionWinningMode, String missionMode, String missionStatus, int startIndex, int pageCount) {
        return this.missionMapper.getMissionByMissionPublisherId(missionPublisherId, missionWinningMode, missionMode, missionStatus, startIndex, pageCount);
    }

    public int getMissionAmountByMissionPublisherId(long missionPublisherId, String missionWinningMode, String missionMode, String missionStatus) {
        return this.missionMapper.getMissionAmountByMissionPublisherId(missionPublisherId, missionWinningMode, missionMode, missionStatus);
    }

    public List<MissionModel> getAllMission(String missionWinningMode, String missionMode, String missionStatus, int currentPage, int pageCount) {
        return this.missionMapper.getAllMission(missionWinningMode, missionMode, missionStatus, currentPage, pageCount);
    }

    public int getAllMissionAmount(String missionWinningMode, String missionMode, String missionStatus) {
        return this.missionMapper.getAllMissionAmount(missionWinningMode, missionMode, missionStatus);
    }

    public List<MissionModel> getRecentlyMission() {
        return this.missionMapper.getRecentlyMission();
    }

    public MissionModel getMissionById(long missionId) {
        return this.missionMapper.getMissionById(missionId);
    }

    public MissionDetailModel getMissionById(long missionId, long userId) {
        MissionModel missionModel = this.missionMapper.getMissionById(missionId);
        UserModel userModel = this.userMapper.getUserById(missionModel.getMissionPublisherId());
        MissionUserRelationshipModel missionUserRelationshipModel = this.missionUserRelationshipMapper.getMissionUserRelationshipByMissionIdAndUserId(missionId, userId);
        MissionDetailModel missionDetailModel = new MissionDetailModel();
        missionDetailModel.setMissionModel(missionModel);
        missionDetailModel.setMissionPublisher(userModel);
        missionDetailModel.setMissionUserRelationshipModel(missionUserRelationshipModel);
        return missionDetailModel;
    }

    public void acceptMission(long missionId) {
        this.missionMapper.acceptMission(missionId);
    }
}
