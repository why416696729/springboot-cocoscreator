package com.wuwhy.whytest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wuwhy.whytest.entity.ResponseResult;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 照片上传工具类
 * @author admin
 *
 */
@Controller
@RequestMapping("/business")
public class UploaderController {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UploaderController.class);

    @Value("${upload.path:#{systemProperties['user.home']}}")
    private String uploadPath;

    @ResponseBody
    @RequestMapping("/uploadImg")
    public void uploadPicture(@RequestParam(value="file",required=false)MultipartFile file,HttpServletRequest request,HttpServletResponse response){
        ResponseResult result = new ResponseResult();
        Map<String, Object> map = new HashMap<String, Object>();
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String exts = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileName = UUID.randomUUID().toString() + "." + exts;
        String fullName = this.getFileStoragePath(uploadPath + "/image/",
                file.getContentType(), fileName);
        try (BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(new File(fullName)))) {
            out.write(file.getBytes());
            out.flush();
                result.setCode(0);
                result.setMessage("图片上传成功");
                map.put("url", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/image/" + fileName);
                LOGGER.info("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/image/" + fileName);
                result.setResult(map);
                writeJson(response, result);

        } catch (Exception e) {
            LOGGER.info("Error occurs while save upload image.", e);
        }
    }

    /**
     * 获取图片存储路径
     *
     * @param path        the path
     * @param contentType the contentType
     * @param fileName    the filename
     * @return the int
     */
    private String getFileStoragePath(String path, String contentType, String fileName) {
        String separator = File.separator;
        String suffix = "png|jp(e)?g|bmp";
        Pattern r = Pattern.compile(suffix);
        Matcher m = r.matcher(contentType);
        if (StringUtils.isBlank(contentType) || !contentType.startsWith("image/") || !m.find()) {
            throw new IllegalArgumentException("文件类型不匹配");
        }
        java.io.File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return String.format("%s%s%s", path, separator, fileName);
    }

    /**
     * 输出JSON数据
     *
     * @param response
     * @param jsonStr
     */
    public void writeJson(HttpServletResponse response, String jsonStr) {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(jsonStr);
            pw.flush();
        } catch (Exception e) {
            LOGGER.info("输出JSON数据异常", e);
        }finally{
            if(pw!=null){
                pw.close();
            }
        }
    }
    /**
     *
     * 向页面响应json字符数组串流.
     *
     * @param response
     * @param jsonStr
     * @throws IOException
     * @return void
     * @author 蒋勇
     * @date 2015-1-14 下午4:18:33
     */
    public void writeJsonStr(HttpServletResponse response, String jsonStr) throws IOException {

        OutputStream outStream = null;
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            outStream = response.getOutputStream();
            outStream.write(jsonStr.getBytes("UTF-8"));
            outStream.flush();
        } catch (IOException e) {
            LOGGER.info("输出JSON数据异常(writeJsonStr)", e);
        } finally {
            if(outStream!=null){
                outStream.close();
            }
        }
    }

    public void writeJsonStr(HttpServletResponse response, InputStream in) throws IOException {

        if(null == in ){
            return ;
        }
        OutputStream outStream = null;
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            outStream = response.getOutputStream();
            int len = 0;
            byte[] byt = new byte[1024];
            while ((len = in.read(byt)) != -1) {
                outStream.write(byt, 0, len);
            }
            outStream.flush();

        } catch (IOException e) {

            LOGGER.info("输出JSON数据异常(writeJsonStr)", e);
        } finally {
            if(outStream!=null){
                outStream.close();
                in.close();
            }
        }
    }


    /**
     * 输出JSON数据
     *
     * @param response
     * @param jsonStr
     */
    public void writeJson(HttpServletResponse response, Object obj) {
        response.setContentType("text/json;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter pw = null;
        Gson gson = new Gson();
        try {
            pw = response.getWriter();
            pw.write(gson.toJson(obj));

            pw.flush();
        } catch (Exception e) {
            LOGGER.info("输出JSON数据异常", e);
        }finally{
            if(pw!=null){
                pw.close();
            }
        }
    }




    public void writeHtml(HttpServletResponse response, String html) {
        response.setContentType("text/html;;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(html);
            pw.flush();
        } catch (Exception e) {
            LOGGER.info("输出HTML数据异常", e);
        }finally{
            if(pw!=null){
                pw.close();
            }
        }
    }

}