package com.scu.scuWitkey.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public static void resizeImageByWidth(InputStream inputStream, File descFile, int width, int height) {
        try {
            BufferedImage bis = ImageIO.read(inputStream);
            int w = bis.getWidth();
            int h = bis.getHeight();
            int nw = width;
            int nh = (nw * h) / w;
            if (nh > height) {
                nh = height;
                nw = (nh * w) / h;
            }
            double sx = (double) nw / w;
            double sy = (double) nh / h;
            AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), null);
            BufferedImage bid = new BufferedImage(nw, nh,
                    BufferedImage.TYPE_3BYTE_BGR);
            ato.filter(bis, bid);
            ImageIO.write(bid, "jpg", descFile);
        } catch (Exception e) {
            logger.error("create thumbnail image failed", e);
        }
    }

    public static void resizeImage(InputStream inputStream, File descFile, double rate) {
        try {
            BufferedImage bis = ImageIO.read(inputStream);
            int w = bis.getWidth();
            int h = bis.getHeight();
            int nw = (int) (w * rate);
            int nh = (int) (h * rate);

            AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(rate, rate), null);
            BufferedImage bid = new BufferedImage(nw, nh,
                    BufferedImage.TYPE_3BYTE_BGR);
            ato.filter(bis, bid);
            ImageIO.write(bid, "jpg", descFile);
        } catch (Exception e) {
            logger.error("create thumbnail image failed", e);
        }
    }

    public static void resizeImageByHeight(InputStream inputStream, File descFile, int width, int height) {
        try {
            BufferedImage bis = ImageIO.read(inputStream);
            int w = bis.getWidth();
            int h = bis.getHeight();
            int nw = (height * w) / h;
            BufferedImage bid = new BufferedImage(nw, height, BufferedImage.TYPE_USHORT_555_RGB);
            if (width < nw) {
                int x = (nw - width) / 2;
                bid.getGraphics().drawImage(bis.getScaledInstance(nw, height,
                        Image.SCALE_SMOOTH), 0, 0, null);
                bid = bid.getSubimage(x, 0, width, height);
            } else {
                bid.getGraphics().drawImage(
                        bis.getScaledInstance(nw, height,
                                Image.SCALE_SMOOTH), 0, 0, null);
            }
            ImageIO.write(bid, "jpg", descFile);

        } catch (Exception e) {
            logger.error("create thumbnail image failed", e);
        }
    }

    public static void resizeImageByWidthAndHeightSmooth(InputStream inputStream, File descFile, int width, int height) {
        try {
            BufferedImage bis = ImageIO.read(inputStream);
            BufferedImage bid = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
            bid.getGraphics().drawImage(
                    bis.getScaledInstance(width, height,
                            Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(bid, "jpg", descFile);

        } catch (Exception e) {
            logger.error("create thumbnail image failed", e);
        }
    }

    public static void resizeImageByWidthAndHeight(InputStream inputStream, File descFile, int width, int height) {
        try {
            BufferedImage bis = ImageIO.read(inputStream);
            int w = bis.getWidth();
            int h = bis.getHeight();
            double sx = (double) width / w;
            double sy = (double) height / h;
            AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), null);
            BufferedImage bid = new BufferedImage(width, height,
                    BufferedImage.TYPE_3BYTE_BGR);
            ato.filter(bis, bid);
            ImageIO.write(bid, "jpg", descFile);
        } catch (Exception e) {
            logger.error("create thumbnail image failed", e);
        }
    }

    public static void cutImage(InputStream inputStream, File outFile, int x, int y, int width, int height) {
        try {
            BufferedImage bid = ImageIO.read(inputStream);
            if (bid.getWidth() > (x + width) && bid.getHeight() > (y + height)) {
                bid = bid.getSubimage(x, y, width, height);
            }
            ImageIO.write(bid, "jpg", outFile);
        } catch (Exception e) {
            logger.error("create thumbnail image failed", e);
        }
    }

    public static void createAvatar(InputStream inputStream, File outFile, int x, int y, int width, int height, int newWidth,int newHeight) {
        try {
            BufferedImage bis = ImageIO.read(inputStream);
            if (bis.getWidth() > (x + width) && bis.getHeight() > (y + height)) {
                bis = bis.getSubimage(x, y, width, height);
                int w = bis.getWidth();
                int h = bis.getHeight();
                double sx = (double) newWidth / w;
                double sy = (double) newHeight / h;
                AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), null);
                BufferedImage bid = new BufferedImage(newWidth, newHeight,
                        BufferedImage.TYPE_3BYTE_BGR);
                ato.filter(bis, bid);
                ImageIO.write(bid, "jpg", outFile);
            }
        } catch (Exception e) {
            logger.error("create createAvatar image failed", e);
        }
    }
}
