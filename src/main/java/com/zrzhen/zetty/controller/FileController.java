package com.zrzhen.zetty.controller;


import com.zrzhen.zetty.core.http.HttpHeaders;
import com.zrzhen.zetty.core.http.Multipart;
import com.zrzhen.zetty.core.http.Request;
import com.zrzhen.zetty.core.http.Response;
import com.zrzhen.zetty.core.mvc.ContentTypeEnum;
import com.zrzhen.zetty.core.mvc.anno.*;
import com.zrzhen.zetty.core.util.FileUtil;
import com.zrzhen.zetty.core.util.ProUtil;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
import com.zrzhen.zetty.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * @author chenanlian
 */
@Controller("/file/")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    /**
     * @return
     */
    @BeforeAdviceAction(id = "adiminBeforeAdvice")
    @ContentType(ContentTypeEnum.HTML)
    @RequestMapping(value = "upload.html")
    public String upload() {
        return "upload.html";
    }

    @BeforeAdviceAction(id = "adiminBeforeAdvice")
    @ContentType(ContentTypeEnum.HTML)
    @RequestMapping("download.html")
    public String download() {
        return "download.html";
    }

    /**
     * 查看文件列表
     *
     * @param group
     * @return
     */
    @BeforeAdviceAction(id = "adiminBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("fileList")
    public Result fileList(@RequestParam(name = "group", defaultValue = "/") String group) {

        if (!group.startsWith("/")) {
            group = "/" + group;
        }
        if (!group.endsWith("/")) {
            group = group + "/";
        }

        String destDir = ProUtil.getString("upload.filePath") + group;

        try {
            File[] files = new File(destDir).listFiles();
            List<String> directoryList = new ArrayList<>();
            List<String> fileList = new ArrayList<>();
            for (File file : files) {
                boolean isDir = file.isDirectory();
                String name = file.getName();
                if (isDir) {
                    directoryList.add(name);
                } else {
                    fileList.add(name);
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("directoryList", directoryList);
            data.put("fileList", fileList);
            data.put("group", group);
            return ResultGen.genResult(ResultCode.SUCCESS, data);
        } catch (Exception e) {
            log.error("destDir:{}，msg:{}", destDir, e);
            return ResultGen.genResult(ResultCode.FAIL);
        }
    }

    /**
     * 文件上传
     *
     * @param request
     * @param group
     * @return
     */
    @BeforeAdviceAction(id = "adiminBeforeAdvice")
    @RequestMapping(value = "upload")
    public Result upload(@RequestAnno Request request,
                         @RequestParam(name = "group", defaultValue = "/default") String group) {

        if (!group.startsWith("/")) {
            group = "/" + group;
        }
        String destDir = ProUtil.getString("upload.filePath") + group + "/";

        Multipart multipart = request.getMultipart();

        String fileNameWhole = multipart.getFilename();
        Integer indexOfDot = fileNameWhole.lastIndexOf(".");
        String suffixName = "";
        String fileName = "";
        if (indexOfDot > 0) {
            suffixName = fileNameWhole.substring(indexOfDot);
            fileName = fileNameWhole.substring(0, indexOfDot);
        } else {
            fileName = fileNameWhole;
        }

        String date = TimeUtil.date2Str(new Date(), "yyyyMMddHHmmssS");
        String newFileName = fileName + "-DATE-" + date + suffixName;
        String destPath = destDir + newFileName;
        if (FileUtil.byte2File(multipart.getBody(), destPath)) {
            return ResultGen.genResult(ResultCode.SUCCESS, destPath);
        } else {
            return ResultGen.genResult(ResultCode.FAIL, destPath);
        }
    }

    /**
     * 文件下载
     *
     * @param group
     * @param fileName
     * @return
     * @throws IOException
     */
    @BeforeAdviceAction(id = "adiminBeforeAdvice")
    @RequestMapping(value = "download/{fileName}")
    public Response download(@RequestParam(name = "group", defaultValue = "/default") String group,
                             @PathVariable("fileName") String fileName) throws IOException {

        String path = ProUtil.getString("upload.filePath") + group + "/" + fileName;
        fileName = URLDecoder.decode(fileName, "UTF-8");
        String contentType = FileUtil.contentTypeByFileName(fileName);

        // 下载之后需要在请求头中放置文件名，该文件名按照ISO_8859_1编码。
        String filenames = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        Response response = Response.get();
        byte[] bytes = Files.readAllBytes(new File(path).toPath());
        response.setBody(bytes);
        response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, contentType);
        //response.headers().set(HttpHeaders.Names.CONTENT, "attachment; filename=\"" + filenames+"\"");
        return response;
    }


}
