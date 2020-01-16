package com.zrzhen.sqlgraph.controller;


import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.Multipart;
import com.zrzhen.zetty.http.http.Request;
import com.zrzhen.zetty.http.http.Response;
import com.zrzhen.zetty.http.mvc.anno.*;
import com.zrzhen.zetty.http.util.ProUtil;
import com.zrzhen.zetty.http.util.ServerUtil;
import com.zrzhen.zetty.common.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;

/**
 * @author chenanlian
 * <p>
 * 百度富文本控制类
 */
@Controller("/ueditor/")
public class UeditorController {

    private static final Logger log = LoggerFactory.getLogger(UeditorController.class);


    /**
     * 此处UE配置是默认的，实际上的上传配置项不在此处配置，此处只是给UE编辑器前台调用避免报初始化错误
     *
     * @return
     */
    @RequestMapping(value = "/config")
    public String config(@RequestAnno Request request,
                         @RequestParam(name = "action", required = true) String action,
                         @RequestParam(name = "sp", defaultValue = "false") Boolean sp,
                         @RequestParam(name = "callback") String callback) {
        if (action.equalsIgnoreCase("config")) {
            return actionConfig();
        } else if (action.equalsIgnoreCase("uploadimage")) {
            Multipart multipart = request.getMultipart();
            return uploadFile("image", ProUtil.getString("ueditor.upload.dir"),
                    ProUtil.getString("ueditor.upload.domain"), multipart, callback);
        } else if (action.equalsIgnoreCase("uploadfile")) {
            Multipart multipart = request.getMultipart();
            return uploadFile("file", ProUtil.getString("ueditor.upload.dir"),
                    ProUtil.getString("ueditor.upload.domain"), multipart, callback);
        }
        return actionConfig();
    }


    /**
     * 下载文件或显示图片
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    @RequestMapping("/static/{fileName}")
    public Response download(@PathVariable String fileName) throws IOException {

        fileName = URLDecoder.decode(fileName, "UTF-8");
        String contentType = ServerUtil.contentTypeByFileName(fileName);
        Response response = Response.get();
        response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, contentType);
        String filePath = ProUtil.getString("ueditor.upload.dir") + File.separator + fileName;
        byte[] bytes = Files.readAllBytes(new File(filePath).toPath());
        response.setContent(bytes);
        response.getHeaders().put(HttpHeaders.Names.CACHE_CONTROL, "no-cache, no-store");
        response.getHeaders().put(HttpHeaders.Names.PRAGMA, "no-cache");
        long time = System.currentTimeMillis();
        response.getHeaders().put(HttpHeaders.Names.LAST_MODIFIED, String.valueOf(time));
        response.getHeaders().put(HttpHeaders.Names.DATE, String.valueOf(time));
        response.getHeaders().put(HttpHeaders.Names.EXPIRES, String.valueOf(time));

        return response;
    }


    /**
     * 返回配置信息
     *
     * @return
     */
    private String actionConfig() {
        return "{\n" +
                "    \"imageActionName\": \"uploadimage\",\n" +
                "    \"imageFieldName\": \"upfile\",\n" +
                "    \"imageMaxSize\": 20480000,\n" +
                "    \"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"],\n" +
                "    \"imageCompressEnable\": true,\n" +
                "    \"imageCompressBorder\": 1600,\n" +
                "    \"imageInsertAlign\": \"none\",\n" +
                "    \"imageUrlPrefix\": \"\",\n" +
                "    \"imagePathFormat\": \"/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "\n" +
                "\n" +
                "    \"scrawlActionName\": \"uploadscrawl\",\n" +
                "    \"scrawlFieldName\": \"upfile\",\n" +
                "    \"scrawlPathFormat\": \"/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "    \"scrawlMaxSize\": 20480000,\n" +
                "    \"scrawlUrlPrefix\": \"\",\n" +
                "    \"scrawlInsertAlign\": \"none\",\n" +
                "\n" +
                "\n" +
                "    \"snapscreenActionName\": \"uploadimage\",\n" +
                "    \"snapscreenPathFormat\": \"/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "    \"snapscreenUrlPrefix\": \"\",\n" +
                "    \"snapscreenInsertAlign\": \"none\",\n" +
                "\n" +
                "\n" +
                "    \"catcherLocalDomain\": [\"127.0.0.1\", \"localhost\", \"img.baidu.com\"],\n" +
                "    \"catcherActionName\": \"catchimage\",\n" +
                "    \"catcherFieldName\": \"source\",\n" +
                "    \"catcherPathFormat\": \"/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "    \"catcherUrlPrefix\": \"\",\n" +
                "    \"catcherMaxSize\": 20480000,\n" +
                "    \"catcherAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"],\n" +
                "\n" +
                "\n" +
                "    \"videoActionName\": \"uploadvideo\",\n" +
                "    \"videoFieldName\": \"upfile\",\n" +
                "    \"videoPathFormat\": \"/upload/video/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "    \"videoUrlPrefix\": \"\",\n" +
                "    \"videoMaxSize\": 1024000000,\n" +
                "    \"videoAllowFiles\": [\n" +
                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\"],\n" +
                "\n" +
                "\n" +
                "    \"fileActionName\": \"uploadfile\",\n" +
                "    \"fileFieldName\": \"upfile\",\n" +
                "    \"filePathFormat\": \"/upload/file/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "    \"fileUrlPrefix\": \"\",\n" +
                "    \"fileMaxSize\": 512000000,\n" +
                "    \"fileAllowFiles\": [\n" +
                "        \".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\",\n" +
                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\",\n" +
                "        \".rar\", \".zip\", \".tar\", \".gz\", \".7z\", \".bz2\", \".cab\", \".iso\",\n" +
                "        \".doc\", \".docx\", \".xls\", \".xlsx\", \".ppt\", \".pptx\", \".pdf\", \".txt\", \".md\", \".xml\"\n" +
                "    ],\n" +
                "\n" +
                "\n" +
                "    \"imageManagerActionName\": \"listimage\",\n" +
                "    \"imageManagerListPath\": \"/upload/\",\n" +
                "    \"imageManagerListSize\": 20,\n" +
                "    \"imageManagerUrlPrefix\": \"\",\n" +
                "    \"imageManagerInsertAlign\": \"none\",\n" +
                "    \"imageManagerAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"],\n" +
                "\n" +
                "\n" +
                "    \"fileManagerActionName\": \"listfile\",\n" +
                "    \"fileManagerListPath\": \"/upload/file/\",\n" +
                "    \"fileManagerUrlPrefix\": \"\",\n" +
                "    \"fileManagerListSize\": 20,\n" +
                "    \"fileManagerAllowFiles\": [\n" +
                "        \".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\",\n" +
                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\",\n" +
                "        \".rar\", \".zip\", \".tar\", \".gz\", \".7z\", \".bz2\", \".cab\", \".iso\",\n" +
                "        \".doc\", \".docx\", \".xls\", \".xlsx\", \".ppt\", \".pptx\", \".pdf\", \".txt\", \".md\", \".xml\"\n" +
                "    ]\n" +
                "\n" +
                "}";
    }


    private static String uploadFile(String type, String webUploadDir, String webUploadDomain, Multipart multipart, String callback) {
        if (!webUploadDir.endsWith("/")) {
            webUploadDir += "/";
        }
        if (!webUploadDomain.endsWith("/")) {
            webUploadDomain += "/";
        }

        String[] fileAllowType = {".gif", ".png", ".jpg", ".jpeg", ".bmp"};
        String fileOldName = multipart.getFilename();

        Integer indexOfDot = fileOldName.lastIndexOf(".");
        String fileName = "";
        String fileExt = "";
        if (indexOfDot > 0) {
            fileExt = fileOldName.substring(indexOfDot);
            fileName = fileOldName.substring(0, indexOfDot);
        } else {
            fileName = fileOldName;
        }

        if (!Arrays.asList(fileAllowType).contains(fileExt) && type.equals("image")) {
            return "不允许的文件类型！";
        }

        String date = TimeUtil.date2Str(new Date(), "yyyyMMddHHmmssS");
        String fileNewName = fileName + "-DATE-" + date + fileExt;
        String saveFileName = webUploadDir + fileNewName;
        File targFile = new File(saveFileName);
        //FileUtil.byte2File(multipart.getBody(),saveFileName);
        boolean copyResult = FileUtil.copyFileUsingFileChannels(new File(multipart.getTmpPath()), targFile);

        String result = "{\"name\":\"" + saveFileName + "\", \"originalName\": \"" + saveFileName + "\", \"size\": " + targFile.length() + ", \"state\": \"" + "SUCCESS" + "\", \"type\": \"" + fileExt + "\", \"url\": \"" + webUploadDomain + fileNewName + "\"}";
        result = result.replaceAll("\\\\", "\\\\");

        if (callback != null && callback.equals("fileName") && copyResult) {
            return webUploadDomain + fileNewName;
        } else if (callback == null) {
            return result;
        } else {
            return "<script>" + callback + "(" + result + ")</script>";
        }
    }

}
