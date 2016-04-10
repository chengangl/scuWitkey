package com.scu.scuWitkey.web;

import com.scu.scuWitkey.Constant.Constants;
import com.scu.scuWitkey.core.domain.UserModel;
import com.scu.scuWitkey.core.service.FileService;
import com.scu.scuWitkey.core.service.UserService;
import com.scu.scuWitkey.core.utils.JsonUtil;
import com.sina.sae.memcached.SaeMemcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    private SaeMemcache saeMemcache = new SaeMemcache();

    @RequestMapping(value = "/login", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,HttpSession session) {
        logger.info("login---sessionID---" + session.getId());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (email.isEmpty() || password.isEmpty()) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        UserModel userModel = this.userService.getUser(email, password);
        logger.info("login---userModel---" + JsonUtil.toJson(userModel));
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_FAILURE);
        } else {
            saeMemcache.init();
            saeMemcache.set(session.getId(), userModel, Constants.HTTP_SESSION_INVALID_TIME);
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
            resultMap.put("user", userModel);
        }
        logger.info("loginEnd---saeMemcache---" + saeMemcache.get(session.getId()));
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/logOut", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String logOut(HttpSession session) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        saeMemcache.delete(session.getId());
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/loginValidate", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String indexLoginValidate(HttpSession session) {
        logger.info("login---sessionID---" + session.getId());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        boolean userShow = false;
        saeMemcache.init();
        UserModel currentUser = saeMemcache.get(session.getId());
        logger.info("loginEnd---saeMemcache---" + saeMemcache.get(session.getId()));
        if (null != currentUser) {
            userShow = true;
        }
        logger.info("indexLoginValidate---userShow---" + userShow);
        resultMap.put("userShow", userShow);
        resultMap.put("user", currentUser);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/register", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody String userJsonBody,HttpSession session) {
        logger.info("register---userJsonBody = " + userJsonBody);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        UserModel userModel = JsonUtil.fromJson(userJsonBody, UserModel.class);
        UserModel userExist = this.userService.getUserByEmail(userModel.getEmail());
        if (null != userExist) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_REGISTER_USER_EXIST);
            resultMap.put("data", userExist.getEmail());
            return JsonUtil.toJson(resultMap);
        }
        userModel.setAvatar(Constants.USER_DEFAULT_AVATAR);
        this.userService.insertUser(userModel);
        userModel = this.userService.getUser(userModel.getEmail(), userModel.getPassword());
        saeMemcache.init();
        saeMemcache.set(session.getId(), userModel, Constants.HTTP_SESSION_INVALID_TIME);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", userModel);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/updatePersonalInformation", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String updatePersonalInformation(@RequestBody String userJsonBody,HttpSession session) {
        logger.info("updatePersonalInformation---userJsonBody = " + userJsonBody);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel currentUser = saeMemcache.get(session.getId());
        if (null == currentUser) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        UserModel userPersonalInformation = JsonUtil.fromJson(userJsonBody, UserModel.class);
        if (currentUser.getId() != userPersonalInformation.getId()) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_AUTHORIZE_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        this.userService.updateUser(userPersonalInformation);
        currentUser = this.userService.getUser(currentUser.getEmail(), currentUser.getPassword());
        saeMemcache.init();
        saeMemcache.set(session.getId(), currentUser, Constants.HTTP_SESSION_INVALID_TIME);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/uploadAvatarPic", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String uploadAvatarPic(@RequestParam("files") MultipartFile[] files) {
        logger.info("uploadAvatarPic---files" + files[0].getOriginalFilename());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", this.fileService.uploadFilesWithStorage(files));
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/updateAvatar", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String updateAvatar(@RequestParam("id") long id,
                               @RequestParam("avatarPic") String avatarPic,
                               @RequestParam("startX") int startX,
                               @RequestParam("startY") int startY,
                               @RequestParam("avatarCutWidth") int avatarCutWidth,
                               @RequestParam("avatarCutHeight") int avatarCutHeight) {
        logger.info("updateAvatar---id" + id);
        logger.info("updateAvatar---avatarPic" + avatarPic);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String avatar = this.fileService.uploadAvatarWithStorage(avatarPic, startX, startY, avatarCutWidth, avatarCutHeight);
        logger.info("updateAvatar---avatar" + avatar);
        if (!avatar.isEmpty()) {
            this.userService.updateUserAvatar(id, avatar);
        }
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", avatar);
        return JsonUtil.toJson(resultMap);
    }
}