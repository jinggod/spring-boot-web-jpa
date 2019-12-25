package com.frontsurf.springwebjpa.common.utils;

import com.frontsurf.springwebjpa.common.config.GlobalConfig;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.zip.ZipOutputStream;

/**
 * @author zou.shiyong
 * @Description 文件下载操作工具类
 * @date 2019/1/3 17:51
 * @Email zou.shiyong@frontsurf.com
 */
public class FileUtils {


    static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 下载Excel文件模板
     *
     * @param response
     * @param file
     * @throws UnsupportedEncodingException
     */
    public static void downloadFile(HttpServletResponse response, HttpServletRequest request, File file) throws UnsupportedEncodingException, FileNotFoundException {
        // 如果文件名存在，则进行下载
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            downloadFile(response, request, fis, file.getName());
        }
    }


    /**
     * 文件下载
     *
     * @param response
     * @param request
     * @param inputStream 文件的数据流
     * @param fileName    文件名称
     * @throws UnsupportedEncodingException
     */
    public static void downloadFile(HttpServletResponse response, HttpServletRequest request, InputStream inputStream, String fileName) throws UnsupportedEncodingException {

        //处理中文文件名乱码
        /*if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            //非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }*/
        // 配置文件下载
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
//        fileName = URLEncoder.encode(fileName);
//        fileName = new String(fileName.getBytes("ISO8859-1"), "UTF-8");
        System.out.println(fileName);
        //处理中文文件名乱码
        if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            //非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        // 通知浏览器进行文件下载
        response.setContentType("multipart/form-data");
        // 实现文件下载
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(inputStream);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文件保存
     *
     * @param file
     * @return
     * @throws DataException
     */
    public static String saveUploadFile(MultipartFile file) throws DataException {
        if (file.isEmpty()) {
            throw new DataException(Return.VALIDATION_ERROR, "文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        //加个时间戳，尽量避免文件名称重复
        String path = GlobalConfig.FILE_SAVE_PATH + File.separator + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
        File dest = new File(path);
        //判断文件是否已经存在
        if (dest.exists()) {
            throw new DataException(Return.VALIDATION_ERROR, "文件已存在");
        }
        try {
            dest.createNewFile();
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataException(Return.COMMON_ERROR, "保存失败");
        }
        return path;
    }


    /**
     * 上传文件，对上传文件添加 大小，文件类型的拦截
     *
     * @param multipartFile
     * @param size
     * @param fileTypes
     * @return
     * @throws DataException
     */
    public static String saveUploadFile(MultipartFile multipartFile, Long size, Set<String> fileTypes) throws DataException {

        //文件大小判断
        if (size != null && size < multipartFile.getSize()) {
            throw new DataException(Return.VALIDATION_ERROR, "上传失败，上传的文件大小不能超过" + size / 1024.0 + "KB");
        }

        //文件类型判断
        String contentType = multipartFile.getContentType();
        String uploadFileType = contentType.split("/")[1];
        if (fileTypes != null && !fileTypes.contains(uploadFileType)) {
            throw new DataException(Return.VALIDATION_ERROR, "上传失败，允许上传文件的类型包括：" + Arrays.toString(fileTypes.toArray()));
        }
        return saveUploadFile(multipartFile);
    }


    /**
     * 文件zip压缩下载的初始化，response的一些内容设置
     *
     * @param request
     * @param response
     * @param fileName 压缩包的名字
     * @return 压缩的文件输出流
     */
    public static ZipOutputStream zipDownloadInit(HttpServletRequest request, HttpServletResponse response, String fileName) {
        // 配置文件下载
        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            fileName = URLEncoder.encode(fileName);
            //处理中文文件名乱码
            if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                    request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                    || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            } else {
                //非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            // 通知浏览器进行文件下载
            response.setContentType("multipart/form-data");
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            return zipOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
