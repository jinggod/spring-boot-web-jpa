package com.frontsurf.springwebjpa.controller.demo;

import com.frontsurf.springwebjpa.common.utils.FileUtils;
import com.frontsurf.springwebjpa.common.utils.IOUtils;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/15 7:34
 * @Email xu.xiaojing@frontsurf.com
 * @Description 文件上传、下载的demo
 * Note :还有一个全局的文件类型拦截器
 */

@RequestMapping("/file")
@RestController
public class FileController {

    /**
     * 单个文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Return uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileUtils.saveUploadFile(file);
        } catch (DataException e) {
            e.printStackTrace();
            return Return.fail(e.getCode(), e.getErrorMessage());
        }
        return Return.success("文件删除成功");
    }

    /**
     * 文件批量上传
     *
     * @param request
     * @return
     */
    @PostMapping("/batch/upload")
    public Return batchUploadFile(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        try {
            for (MultipartFile file : files) {
                FileUtils.saveUploadFile(file);
            }
        } catch (DataException e) {
            e.printStackTrace();
            return Return.fail(e.getCode(), e.getErrorMessage());
        }

        return Return.success("文件上传成功");
    }

    /**
     * 下载一个文件
     *
     * @param response
     * @param request
     * @param fileName
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, HttpServletRequest request, String fileName) {

        try {
            File file = ResourceUtils.getFile("classpath:static/" + fileName);
            FileUtils.downloadFile(response, request, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            response.setStatus(404);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量下载一个文件，即将多个文件一起打包，下载压缩包
     *
     * @param request
     * @param response
     * @param fileNames
     */
    @GetMapping("/batch/download")
    public void batchDownload(HttpServletRequest request, HttpServletResponse response, @RequestParam("fileNames") List<String> fileNames) {

        ZipOutputStream zipOutputStream = FileUtils.zipDownloadInit(request, response, "batch.zip");
        try {
            for (String fileName : fileNames) {
                File file = ResourceUtils.getFile("classpath:static/" + fileName);
                if (file.exists()) {
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOutputStream.putNextEntry(zipEntry);
                    FileInputStream in = new FileInputStream(file);
                    IOUtils.copy(in, zipOutputStream);
                    in.close();
                }
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(404);
        }
    }

}
