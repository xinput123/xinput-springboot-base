package com.xinput.bootbase.util;

import com.google.common.collect.Maps;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: xinput
 * @Date: 2020-08-12 14:08
 */
public class HttpUtils {

    /**
     * 正则：Content-Type中的编码信息
     */
    public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

    /**
     * 正则：匹配meta标签的编码信息
     */
    public static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*?charset\\s*=\\s*['\"]?([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否https
     *
     * @param url URL
     * @return 是否https
     */
    public static boolean isHttps(String url) {
        return url.toLowerCase().startsWith("https");
    }

    /**
     * 将URL参数解析为Map
     *
     * @param url url参数
     * @return
     */
    public static Map<String, List<String>> decodeParamMap(String url) {
        return decodeParamMap(url, HttpConstants.DEFAULT_CHARSET);
    }

    /**
     * 将URL参数解析为Map
     *
     * @param url     参数字符串（或者带参数的Path）
     * @param charset 字符集
     * @return 参数Map
     */
    public static Map<String, List<String>> decodeParamMap(String url, Charset charset) {
        QueryStringDecoder decoderQuery = new QueryStringDecoder(url, charset);
        return decoderQuery.parameters();
    }

    /**
     * 将URL参数解析为Map
     *
     * @param url url参数
     * @return
     */
    public static Map<String, String> decodeParamHashMap(String url) {
        return decodeParamHashMap(url, HttpConstants.DEFAULT_CHARSET);
    }

    /**
     * 将URL参数解析为Map
     *
     * @param url     参数字符串（或者带参数的Path）
     * @param charset 字符集
     * @return 参数Map
     */
    public static Map<String, String> decodeParamHashMap(String url, Charset charset) {
        QueryStringDecoder decoderQuery = new QueryStringDecoder(url, charset);
        Map<String, List<String>> paramMaps = decoderQuery.parameters();

        Map<String, String> maps = Maps.newHashMapWithExpectedSize(paramMaps.size());
        paramMaps.forEach((param, paramValues) -> {
            paramValues.forEach(paramValue -> maps.put(param, paramValue));
        });

        return maps;
    }

}
