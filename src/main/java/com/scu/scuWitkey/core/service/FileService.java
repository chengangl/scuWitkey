package com.scu.scuWitkey.core.service;

import com.scu.scuWitkey.Constant.Constants;
import com.scu.scuWitkey.core.utils.ImageUtils;

import static org.apache.commons.io.IOUtils.closeQuietly;

import com.sina.sae.storage.SaeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private String root;
    private String domainAddress;
    public static final String ImageFolder = "/localImage/";
    public static final String AvatarFolder = "/avatarImage/";
    public static final String ZipFolder = "/zipFile/";

    public String uploadAvatarWithStorage(String avatarPic, int startX, int startY, int avatarCutWidth, int avatarCutHeight) {
        String fileName = "avatar_" + UUID.randomUUID().toString();
        return createAvatarWithStorage(avatarPic, startX, startY, avatarCutWidth, avatarCutHeight, fileName);
    }

    private String createAvatarWithStorage(String avatarPic, int startX, int startY, int avatarCutWidth, int avatarCutHeight, String fileName) {
        if (avatarPic.contains(domainAddress)) {
            avatarPic = avatarPic.replace(domainAddress, root);
        }
        logger.info("createAvatarWithStorage---avatarPic" + avatarPic);
        try {
            FileInputStream inputStream = new FileInputStream(avatarPic);
            File imageThumbnail = File.createTempFile(fileName, "jpg");
            ImageUtils.createAvatar(inputStream, imageThumbnail, startX, startY, avatarCutWidth, avatarCutHeight, Constants.AVATAR_THUMBNAIL_WIDTH, Constants.AVATAR_THUMBNAIL_HEIGHT);
            return saveFileWithStorage(new FileInputStream(imageThumbnail), AvatarFolder, fileName + ".jpg");
        } catch (IOException e) {
            logger.error("createAvatarWithStorage----createTempFile---fileName--" + e.getMessage());
        }
        return null;
    }

    public List<String> uploadFilesWithStorage(MultipartFile[] files) {
        List<String> imgUrls = new ArrayList<String>();
        for (MultipartFile file : files) {
            String imageUrl = processUploadImageWithStorage(file, ImageFolder);
            imgUrls.add(imageUrl);
        }
        return imgUrls;
    }

    private String processUploadImageWithStorage(MultipartFile file, String folder) {
        logger.info("processUploadImageWithStorage---fileName" + file.getOriginalFilename());
        String thumbnailUrl = null;
        try {
            String fileName = "thumb_" + UUID.randomUUID().toString();
            thumbnailUrl = createThumbnailWithStorage(file.getInputStream(), folder, fileName);
            logger.info("processUploadImageWithStorage  thumbnailUrl = " + thumbnailUrl);
        } catch (IOException e) {
            logger.error("processUploadImageWithStorage  exception = " + e.getMessage());
        }
        return thumbnailUrl;
    }

    private String saveFileWithStorage(InputStream fileInputStream, String folder, String fileName) {
        logger.info("saveFileWithStorage---fileName" + fileName);
        FileOutputStream fos = null;
        if (fileInputStream != null) {
            try {
                fos = new FileOutputStream(root + folder + fileName);
                int len = fileInputStream.available();
                byte[] fileByte = new byte[len];
                fileInputStream.read(fileByte);
                fos.write(fileByte);
            } catch (IOException e) {
                logger.info("saveFileWithStorage---exception" + e.getMessage());
            } finally {
                closeQuietly(fos);
            }
        }
        return domainAddress + folder + fileName;
    }

    private String createThumbnailWithStorage(InputStream inputStream, String folder, String fileName) throws IOException {
        File imageThumbnail = File.createTempFile(fileName, "jpg");
        ImageUtils.resizeImageByWidthAndHeight(inputStream, imageThumbnail, Constants.DEFAULT_THUMBNAIL_WIDTH, Constants.DEFAULT_THUMBNAIL_HEIGHT);
        return saveFileWithStorage(new FileInputStream(imageThumbnail), folder, fileName + ".jpg");
    }

    public List<String> uploadZipFileWithStorage(MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<String>();
        for (MultipartFile file : files) {
            String fileUrl = processUploadZipFileWithStorage(file, ZipFolder);
            fileUrls.add(fileUrl);
        }
        return fileUrls;
    }

    private String processUploadZipFileWithStorage(MultipartFile file, String folder) {
        logger.info("processUploadZipFile---fileName" + file.getOriginalFilename());
        String orgName = file.getOriginalFilename();
        String fileType = orgName.substring(orgName.lastIndexOf("."));
        String fileUrl = null;
        try {
            fileUrl = saveZipFileWithStorage(file.getInputStream(), folder, UUID.randomUUID().toString() + fileType);
        } catch (IOException e) {
            logger.error("processUploadZipFile  exception = " + e.getMessage());
        }
        logger.info("processUploadImage  fileUrl = " + fileUrl);
        return fileUrl;
    }

    public void setDomainAddress(String domainAddress) {
        this.domainAddress = domainAddress;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    private String saveZipFileWithStorage(InputStream fileInputStream, String folder, String fileName) {
        logger.info("saveZipFile---fileName" + fileName);
        FileOutputStream fos = null;
        if (fileInputStream != null) {
            try {
                fos = new FileOutputStream(root + folder + fileName);
                int len = fileInputStream.available();
                byte[] fileByte = new byte[len];
                fileInputStream.read(fileByte);
                fos.write(fileByte);
            } catch (IOException e) {
                logger.info("saveZipFileWithStorage---exception" + e.getMessage());
            } finally {
                closeQuietly(fos);
            }
        }
        return domainAddress + folder + fileName;
    }

    public boolean deleteFileWithStorage(String fileUrl) {//todo storage文件删除操作，file读取报错fileNotExist
        logger.info("deleteFileWithStorage---fileUrl" + fileUrl);
        logger.info("deleteFileWithStorage---domainAddress" + domainAddress);
        if (fileUrl.contains(domainAddress)) {
            fileUrl = fileUrl.substring(domainAddress.length());
            logger.info("deleteFileWithStorage---fileUrl---if---==" + fileUrl);
        }
        logger.info("deleteFileWithStorage---fileUrl" + fileUrl);
        SaeStorage saeStorage = new SaeStorage();
        saeStorage.delete(Constants.SAE_STORAGE_DOMAIN, fileUrl);
        return true;
    }
}
