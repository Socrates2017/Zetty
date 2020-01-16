package com.zrzhen.zetty.cms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.mvc.anno.*;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.cms.pojo.result.Result;
import com.zrzhen.zetty.cms.pojo.result.ResultCode;
import com.zrzhen.zetty.cms.pojo.result.ResultGen;
import com.zrzhen.zetty.common.AesUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenanlian
 */

@Controller("/aes/")
public class AesController {


    /**
     * 加密
     *
     * @param params
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("encrypt")
    public Result encrypt(@RequestJsonBody JsonNode params) {

        String key = JsonUtil.getString(params, "key");        ;
        String data = JsonUtil.getString(params, "data");

        if (StringUtils.isBlank(key)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "key");
        } else if (StringUtils.isBlank(data)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "data");
        } else {

            String result = null;
            try {
                result = AesUtil.encrypt(data, key);
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

        String key = JsonUtil.getString(params, "key");        ;
        String data = JsonUtil.getString(params, "data");

        if (StringUtils.isBlank(key)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "key");
        } else if (StringUtils.isBlank(data)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "data");
        } else {
            String result = null;
            try {
                result = AesUtil.decrypt(data, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResultGen.genResult(ResultCode.SUCCESS, result);
        }

    }

}
