package com.scu.scuWitkey.web;

import com.scu.scuWitkey.Constant.Constants;
import com.scu.scuWitkey.core.domain.RecommendModel;
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
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/manager")
public class ManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    private SaeMemcache saeMemcache = new SaeMemcache();

/*    @RequestMapping(value = "/{folder}/{fileName}.{ext}", method = RequestMethod.GET)
    public void download(@PathVariable("folder") String folder,
                         @PathVariable("fileName") String fileName,
                         @PathVariable("ext") String ext,
                         HttpServletResponse response) throws IOException {
        String realFileName = String.format("%s/%s/%s.%s", fileService.getRoot(), folder, fileName, ext);
        System.out.println("download: " + realFileName);
        response.setContentType(getContentType(ext));
        response.addHeader("Content-Disposition", "attachment;filename=\"" + realFileName + "\"");
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(realFileName));
        ServletOutputStream out = response.getOutputStream();
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = inputStream.read(buff, 0, buff.length))) {
            out.write(buff, 0, bytesRead);
        }
        closeQuietly(out);
        closeQuietly(inputStream);
        out.flush();
    }

    private String getContentType(String ext) {
        if ("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "jpe".equalsIgnoreCase(ext)) {
            return "image/jpeg";
        } else if ("gif".equalsIgnoreCase(ext)) {
            return "image/gif";
        } else if ("png".equalsIgnoreCase(ext)) {
            return "image/png";
        }

        return "application/octet-stream";
    }*/

    @RequestMapping(value = "/fetchDefaultImage", method = RequestMethod.GET)
    @ResponseBody
    public String fetchDefaultImage() {
        logger.info("fetchDefaultImage---addRecommendPic = " + Constants.MANAGER_SETTING_ADD_RECOMMEND_PIC_URL);
        logger.info("fetchDefaultImage---defaultRecommendPic = " + Constants.MANAGER_SETTING_DEFAULT_RECOMMEND_PIC_URL);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("addRecommendPic", Constants.MANAGER_SETTING_ADD_RECOMMEND_PIC_URL);
        resultMap.put("defaultRecommendPic", Constants.MANAGER_SETTING_DEFAULT_RECOMMEND_PIC_URL);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/publishRecommend", method = RequestMethod.POST)
    @ResponseBody
    public String publishRecommend(@RequestBody String recommendJsonBody, HttpSession session) {
        logger.info("publishRecommend---recommendJsonBody = " + recommendJsonBody);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        } else if (!Constants.MANAGER_USER_EMAIL.equals(userModel.getEmail())) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_AUTHORIZE_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        RecommendModel recommendModel = JsonUtil.fromJson(recommendJsonBody, RecommendModel.class);
        recommendModel.setRecommendPublisherId(userModel.getId());
        this.userService.insertRecommend(recommendModel);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/deleteRecommend", method = RequestMethod.GET)
    @ResponseBody
    public String deleteRecommend(@RequestParam("id") long id, HttpSession session) {
        logger.info("deleteRecommend---id = " + id);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        } else if (!Constants.MANAGER_USER_EMAIL.equals(userModel.getEmail())) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_AUTHORIZE_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        this.userService.deleteRecommend(id);
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/file/uploadImage", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("files") MultipartFile[] files, HttpSession session) throws IOException {
        logger.info("uploadImage---files" + files[0].getOriginalFilename());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        saeMemcache.init();
        UserModel userModel = saeMemcache.get(session.getId());
        if (null == userModel) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        } else if (!Constants.MANAGER_USER_EMAIL.equals(userModel.getEmail())) {
            resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS_AUTHORIZE_VALIDATE_FAILURE);
            return JsonUtil.toJson(resultMap);
        }
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", this.fileService.uploadFilesWithStorage(files));
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/file/uploadZipFile", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String uploadZipFile(@RequestParam("files") MultipartFile[] files) {
        logger.info("uploadImage---files" + files[0].getOriginalFilename());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", this.fileService.uploadZipFileWithStorage(files));
        return JsonUtil.toJson(resultMap);
    }

    @RequestMapping(value = "/file/deleteFile", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String deleteFile(@RequestParam("fileUrl") String fileUrl) {
        logger.info("deleteFile---fileUrl" + fileUrl);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Constants.HTTP_REQUEST_STATUS_CODE_SUCCESS);
        resultMap.put("data", this.fileService.deleteFileWithStorage(fileUrl));
        return JsonUtil.toJson(resultMap);
    }
}