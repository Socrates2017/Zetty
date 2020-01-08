package com.zrzhen.zetty.http.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.anno.*;
import com.zrzhen.zetty.http.http.util.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.util.RsaUtil;
import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author chenanlian
 */

@Controller("/rsa/")
public class RsaController {

    /**
     * 生成秘钥对
     *
     * @param keySize
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("genKey")
    public Result rsaGenKkey(@RequestParam(name = "keySize", defaultValue = "1024") String keySize) {

        Map map = null;
        try {
            map = RsaUtil.genKeyPair(Integer.valueOf(keySize));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ResultGen.genResult(ResultCode.SUCCESS, map);
    }

    /**
     * 加密
     *
     * @param params
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("encrypt")
    public Result encrypt(@RequestJsonBody JsonNode params) {
        String publicKey = JsonUtil.getString(params, "publicKey").trim();        ;
        String data = JsonUtil.getString(params, "data").trim();    ;
        if (StringUtils.isBlank(publicKey)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "publicKey");
        } else if (StringUtils.isBlank(data)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "data");
        } else {
            String result = null;
            try {
                result = RsaUtil.encrypt(data, publicKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResultGen.genResult(ResultCode.SUCCESS, result);
        }
    }


    /**
     * 解密
     *
     * @param params
     * @return
     */
    @RequestMapping("decrypt")
    public Result decrypt(@RequestJsonBody JsonNode params) {

        String privateKey = JsonUtil.getString(params, "privateKey").trim();
        String data = JsonUtil.getString(params, "data").trim();

        if (StringUtils.isBlank(privateKey)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "publicKey");
        } else if (StringUtils.isBlank(data)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "data");
        } else {
            String result = null;
            try {
                result = RsaUtil.decrypt(data, privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResultGen.genResult(ResultCode.SUCCESS, result);
        }

    }

}
