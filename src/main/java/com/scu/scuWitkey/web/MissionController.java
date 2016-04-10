package com.scu.scuWitkey.web;

import com.scu.scuWitkey.Constant.Constants;
import com.scu.scuWitkey.core.domain.MissionModel;
import com.scu.scuWitkey.core.domain.MissionUserRelationshipModel;
import com.scu.scuWitkey.core.domain.UserModel;
import com.scu.scuWitkey.core.model.MissionDetailModel;
import com.scu.scuWitkey.core.service.MissionService;
import com.scu.scuWitkey.core.service.MissionUserRelationshipService;
import com.scu.scuWitkey.core.service.UserService;
import com.scu.scuWitkey.core.utils.JsonUtil;
import com.sina.sae.memcached.SaeMemcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/mission", produces = "application/json; charset=UTF-8")
@SessionAttributes("currentUser")
public class MissionController {
    private static final Logger logger = LoggerFactory.getLogger(MissionController.class);
    @Autowired
    private MissionService missionService;
    @Autowired
    private MissionUserRelationshipService missionUserRelationshipService;
    @Autowired
    private UserService userService;

    private SaeMemcache saeMemcache = new SaeMemcache();

    @RequestMapping(value = "/publishMission", method = RequestMethod.POST)
    @ResponseBody
    public String publishMission(@RequestBody String missionJsonBody,HttpSession session) {
        logger.info("publishMission---missionJsonBody = " + missionJsonBody);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        MissionModel missionModel = JsonUtil.fromJson(missionJsonBody, MissionModel.class);
        missionModel.setMissionPublisherId(userModel.getId());
        this.missionService.insertMission(missionModel);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/updateMission", method = RequestMethod.POST)
    @ResponseBody
    public String updateMission(@RequestBody String missionJsonBody, HttpSession session) {
        logger.info("updateMission---missionJsonBody = " + missionJsonBody);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        MissionModel missionModel = JsonUtil.fromJson(missionJsonBody, MissionModel.class);
        this.missionService.updateMission(missionModel);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/allMission", method = RequestMethod.GET)
    @ResponseBody
    public String getAllMission(@RequestParam("missionWinningMode") String missionWinningMode,
                                @RequestParam("missionMode") String missionMode,
                                @RequestParam("missionStatus") String missionStatus,
                                @RequestParam("currentPage") int currentPage) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("fetchAllMission---Params = " + missionWinningMode + missionMode + missionStatus);
        List<MissionModel> missionModelList = this.missionService.getAllMission(missionWinningMode, missionMode, missionStatus, (currentPage - 1) * Constants.MISSION_LIST_PAGE_COUNT, Constants.MISSION_LIST_PAGE_COUNT);
        int missionAmount = this.missionService.getAllMissionAmount(missionWinningMode, missionMode, missionStatus);
        int pageAmount = (int) Math.ceil(missionAmount / (double) Constants.MISSION_LIST_PAGE_COUNT);
        pageAmount = pageAmount == 0 ? 1 : pageAmount;
        logger.info("fetchAllMission---missionAmount = " + missionAmount);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", missionModelList);
        resultMap.put("pageAmount", pageAmount);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/getMissionById", method = RequestMethod.GET)
    @ResponseBody
    public String getMissionById(@RequestParam("missionId") long missionId, HttpSession session) {
        logger.info("getMissionById---missionId = " + missionId);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        MissionDetailModel missionDetailModel = this.missionService.getMissionById(missionId, userModel.getId());
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", missionDetailModel);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/getMissionUserRelationshipByMissionId", method = RequestMethod.GET)
    @ResponseBody
    public String getMissionUserRelationshipByMissionId(@RequestParam("missionId") long missionId, HttpSession session) {
        logger.info("getMissionUserRelationshipByMissionId---missionId = " + missionId);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        List<MissionUserRelationshipModel> missionUserRelationshipModelList = this.missionUserRelationshipService.getMissionUserRelationshipByMissionId(missionId);
        List<UserModel> userModelList = new ArrayList<UserModel>();
        for (MissionUserRelationshipModel missionUserRelationshipModel : missionUserRelationshipModelList) {
            userModelList.add(this.userService.getUserById(missionUserRelationshipModel.getUserId()));
        }
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", missionUserRelationshipModelList);
        resultMap.put("userModelList", userModelList);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/acceptMission", method = RequestMethod.GET)
    @ResponseBody
    public String acceptMission(@RequestParam("missionId") long missionId, HttpSession session) {
        logger.info("acceptMission---missionId = " + missionId);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
//        this.missionService.acceptMission(missionId);//todo 不修改任务状态
        MissionUserRelationshipModel missionUserRelationshipModel = new MissionUserRelationshipModel();
        missionUserRelationshipModel.setMissionId(missionId);
        missionUserRelationshipModel.setUserId(userModel.getId());
        missionUserRelationshipModel.setMissionAcceptDate(new Date());
        missionUserRelationshipModel.setMissionFinishDate(null);
        missionUserRelationshipModel.setMissionCompletionStatus("进行中");
        this.missionUserRelationshipService.insertMissionUserRelationship(missionUserRelationshipModel);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/submitMission", method = RequestMethod.GET)
    @ResponseBody
    public String submitMission(@RequestParam("id") long id,
                                @RequestParam("missionSubmitData") String missionSubmitData,
                                @RequestParam("missionSubmitDataName") String missionSubmitDataName,
                                HttpSession session) {
        logger.info("submitMission---id = " + id);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        this.missionUserRelationshipService.updateMissionUserRelationship(id, missionSubmitData, missionSubmitDataName, new Date());
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/bidRelationship", method = RequestMethod.GET)
    @ResponseBody
    public String bidRelationship(@RequestParam("id") long id,
                                  @RequestParam("missionId") long missionId,
                                  @RequestParam("missionWinningMode") String missionWinningMode,
                                  HttpSession session) {
        logger.info("bidRelationship---missionUserRelationshipId = " + id);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        this.missionUserRelationshipService.bidMissionUserRelationship(id);
        if ("单人中标".equals(missionWinningMode)){
            this.missionService.finishMission(missionId);
        }
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/getAcceptMission", method = RequestMethod.GET)
    @ResponseBody
    public String getAcceptMission(@RequestParam("missionWinningMode") String missionWinningMode,
                                   @RequestParam("missionMode") String missionMode,
                                   @RequestParam("missionStatus") String missionStatus,
                                   @RequestParam("currentPage") int currentPage,
                                   HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        missionWinningMode = missionWinningMode.equals("中标模式") ? "" : missionWinningMode;
        missionMode = missionMode.equals("任务模式") ? "" : missionMode;
        missionStatus = missionStatus.equals("任务状态") ? "" : missionStatus;
        logger.info("getAcceptMission---" + missionWinningMode + missionMode + missionStatus);
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        List<MissionUserRelationshipModel> missionUserRelationshipModelList = this.missionUserRelationshipService.getMissionUserRelationshipByUserId(userModel.getId());
        List<MissionModel> missionModelList = new ArrayList<MissionModel>();
        int startIndex = (currentPage - 1) * Constants.MISSION_LIST_PAGE_COUNT;
        int endIndex = startIndex + Constants.MISSION_LIST_PAGE_COUNT;
        int missionAmount = 0;
        for (MissionUserRelationshipModel missionUserRelationshipModel : missionUserRelationshipModelList) {
            MissionModel missionModel = this.missionService.getMissionById(missionUserRelationshipModel.getMissionId());
            if (missionModel != null && compareString(missionModel.getMissionWinningMode(), missionWinningMode)
                    && compareString(missionModel.getMissionMode(), missionMode)
                    && compareString(missionModel.getMissionStatus(), missionStatus)) {
                logger.info("getAcceptMission---missionAmount = " + missionAmount);
                if (missionAmount >= startIndex && missionAmount < endIndex) {
                    missionModelList.add(missionModel);
                }
                missionAmount++;
            }
        }
        int pageAmount = (int) Math.ceil(missionAmount / (double) Constants.MISSION_LIST_PAGE_COUNT);
        pageAmount = pageAmount == 0 ? 1 : pageAmount;
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", missionModelList);
        resultMap.put("pageAmount", pageAmount);
        return JsonUtil.toJson(resultMap);
    }

    private boolean compareString(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    @RequestMapping(value = "/mine/missionPublish", method = RequestMethod.GET)
    @ResponseBody
    public String mineMissionPublish(@RequestParam("missionWinningMode") String missionWinningMode,
                                     @RequestParam("missionMode") String missionMode,
                                     @RequestParam("missionStatus") String missionStatus,
                                     @RequestParam("currentPage") int currentPage,
                                     HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("getAcceptMission---" + missionWinningMode + missionMode + missionStatus + currentPage);
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        logger.info("mineMissionPublish---user = " + userModel.getUserName());
        List<MissionModel> missionModelList = this.missionService.getMissionByMissionPublisherId(userModel.getId(), missionWinningMode, missionMode, missionStatus, (currentPage - 1) * Constants.MISSION_LIST_PAGE_COUNT, Constants.MISSION_LIST_PAGE_COUNT);
        int missionAmount = this.missionService.getMissionAmountByMissionPublisherId(userModel.getId(), missionWinningMode, missionMode, missionStatus);
        int pageAmount = (int) Math.ceil(missionAmount / (double) Constants.MISSION_LIST_PAGE_COUNT);
        pageAmount = pageAmount == 0 ? 1 : pageAmount;
        logger.info("fetchAllMission---missionAmount = " + missionAmount);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", missionModelList);
        resultMap.put("pageAmount", pageAmount);
        return JsonUtil.toJson(resultMap);
    }
}