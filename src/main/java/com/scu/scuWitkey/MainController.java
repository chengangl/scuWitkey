package com.scu.scuWitkey;

import com.scu.scuWitkey.Constant.Constants;
import com.scu.scuWitkey.core.domain.MissionModel;
import com.scu.scuWitkey.core.domain.RecommendModel;
import com.scu.scuWitkey.core.domain.UserModel;
import com.scu.scuWitkey.core.service.MissionService;
import com.scu.scuWitkey.core.service.UserService;
import com.scu.scuWitkey.core.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private MissionService missionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/index/fetchRecommends", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String fetchRecommends() {
        List<RecommendModel> recommendModels = this.userService.fetchRecommends();
        logger.info("fetchRecommends---" + recommendModels);
        return JsonUtil.toJson(recommendModels);
    }

    @RequestMapping(value = "index/recentlyMission", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String getRecentlyMission() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        List<MissionModel> missionModelList = this.missionService.getRecentlyMission();
        logger.info("getRecentlyMission---missionModelList = " + missionModelList);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", missionModelList);
        return JsonUtil.toJson(resultMap);
    }
}