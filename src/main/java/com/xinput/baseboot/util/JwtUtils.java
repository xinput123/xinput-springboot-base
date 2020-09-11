package com.xinput.baseboot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.google.common.collect.Maps;
import com.xinput.baseboot.config.BaseBootConfig;
import com.xinput.baseboot.domain.JwtToken;
import com.xinput.bleach.consts.BaseConsts;
import com.xinput.bleach.util.BeanUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author xinput
 * @date 2020-06-15 15:35
 */
public class JwtUtils {

    public static final String AUD = "aud";

    public static final String PLATFORM = "platform";

    public static final String EXP = "exp";

    public static final String IAT = "iat";

    public static final String TOKEN = "token";

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(BaseBootConfig.getApiSecureKey());

    public static String sign(String aud) {
        return sign(aud, BaseConsts.DEFAULT);
    }

    public static String sign(Object aud) {
        return sign(aud, BaseConsts.DEFAULT);
    }

    public static String sign(Object aud, String platform) {
        return sign(aud, platform, Maps.newHashMap());
    }

    public static String sign(Object aud, Map<String, Object> claims) {
        return sign(aud, BaseConsts.DEFAULT, claims);
    }

    /**
     * @param aud      用户id
     * @param platform 用户平台，主要分web端、app端
     * @param claims
     * @return
     */
    public static String sign(Object aud, String platform, Map<String, Object> claims) {
        // 1 签发Token
        Date currentTime = new Date();
        JWTCreator.Builder builder = JWT.create()
                // 发行者
//                .withIssuer("xinput")
                // 用户身份标识
//                .withSubject("xinput")
                // 用户单位
//                .withAudience("xinput")
                // 签发时间
                .withIssuedAt(currentTime)
                .withExpiresAt(new Date(currentTime.getTime() + 1000L * Long.valueOf(BaseBootConfig.getTokenExp())))
//                .withJWTId("001") // 分配JWT的ID
                // 定义公共域信息
                .withClaim(AUD, String.valueOf(aud))
                .withClaim(PLATFORM, platform);
        claims.forEach((key, value) -> {
            builder.withClaim(key, String.valueOf(value));
        });

        return builder.sign(ALGORITHM);
    }

    public static Map<String, Object> verify(String jwt) throws TokenExpiredException, JWTVerificationException {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        Map<String, Claim> claims = verifier.verify(jwt).getClaims();
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(claims.size());
        claims.forEach((k, v) -> {
            if (AUD.equalsIgnoreCase(k) || PLATFORM.equalsIgnoreCase(k)) {
                map.put(k, v.asString());
                return;
            }
            if (IAT.equalsIgnoreCase(k) || EXP.equalsIgnoreCase(k)) {
                map.put(k, v.asLong());
                return;
            }
        });

        return map;
    }

    public static JwtToken verifyJwtToken(String jwt) throws TokenExpiredException, JWTVerificationException {
        return BeanUtils.convertor(verify(jwt), JwtToken.class);
    }
}
