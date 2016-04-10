package com.scu.scuWitkey.core.mapper;

import com.scu.scuWitkey.core.domain.MissionModel;
import com.scu.scuWitkey.core.domain.MissionUserRelationshipModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MissionMapper {
    void insertMission(MissionModel missionModel);

    void updateMission(MissionModel missionModel);

    void finishMission(@Param("id")long id);

    void updateMissionStatusPending();

    void updateMissionStatusFinished();

    List<MissionModel> getMissionByMissionPublisherId(@Param("missionPublisherId")long missionPublisherId,@Param("missionWinningMode")String missionWinningMode,  @Param("missionMode")String missionMode, @Param("missionStatus")String missionStatus, @Param("startIndex")int startIndex, @Param("pageCount")int pageCount);

    int getMissionAmountByMissionPublisherId(@Param("missionPublisherId")long missionPublisherId,@Param("missionWinningMode")String missionWinningMode,  @Param("missionMode")String missionMode, @Param("missionStatus")String missionStatus);

    List<MissionModel> getAllMission(@Param("missionWinningMode") String missionWinningMode, @Param("missionMode") String missionMode, @Param("missionStatus") String missionStatus, @Param("currentPage") int currentPage, @Param("pageCount")int pageCount);

    int getAllMissionAmount(@Param("missionWinningMode") String missionWinningMode, @Param("missionMode") String missionMode, @Param("missionStatus") String missionStatus);

    List<MissionModel> getRecentlyMission();

    MissionModel getMissionById(long missionId);

    void acceptMission(long missionId);
}
