package com.xinput.bootbase.util;

import com.xinput.bleach.util.JsonUtils;
import com.xinput.bleach.util.Logs;
import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.domain.WechatUserInfo;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * 微信code验证
 */
public class WechatUtils {

    private static final Logger logger = Logs.get();

    /**
     * 接收从客户端获取的code， 向微信后台发起请求获取openid的url
     */
    private static String wxUrl = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=#{3}";

    public static WechatUserInfo code2Session(String code) {
        String requestUrl = MessageFormat.format(wxUrl, DefaultConfig.getWechatAppid(), DefaultConfig.getWechatSecret(), code, "authorization_code");

        WechatUserInfo authInfo = null;
        try {
            String result = Request.Get(requestUrl).execute().returnContent().asString();
            System.out.println(result);
            logger.info("code2session信息:[{}]", result);
            authInfo = JsonUtils.toBean(result, WechatUserInfo.class);
        } catch (ClientProtocolException e) {
            logger.error("登录凭证校验失败,code:[{}].", code, e);
        } catch (IOException e) {
            logger.error("登录凭证校验失败,code:[{}]", code, e);
        }

        return authInfo;
    }

}
