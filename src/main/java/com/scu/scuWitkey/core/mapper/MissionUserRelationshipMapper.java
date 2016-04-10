package com.scu.scuWitkey.core.mapper;

import com.scu.scuWitkey.core.domain.MissionUserRelationshipModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MissionUserRelationshipMapper {
    void insertMissionUserRelationship(MissionUserRelationshipModel missionUserRelationshipModel);

    void bidMissionUserRelationship(@Param("id") long id);

    void updateMissionUserRelationship(@Param("id") long id, @Param("missionSubmitData") String missionSubmitData, @Param("missionSubmitDataName") String missionSubmitDataName, @Param("missionFinishDate") Date missionFinishDate);

    List<MissionUserRelationshipModel> getMissionUserRelationshipByUserId(long userId);

    MissionUserRelationshipModel getMissionUserRelationshipByMissionIdAndUserId(@Param("missionId") long missionId, @Param("userId") long userId);

    List<MissionUserRelationshipModel> getMissionUserRelationshipByMissionId(long missionId);
}
