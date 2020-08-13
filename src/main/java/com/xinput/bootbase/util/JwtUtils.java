package com.xinput.bootbase.util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.google.common.collect.Maps;
import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.consts.BaseConsts;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xinput
 * @Date: 2020-06-15 15:35
 */
public class JwtUtils {

    public static final String AUD = "aud";

    public static final String PLATFORM = "platform";

    public static final String EXP = "exp";

    public static final String IAT = "iat";

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
        return sign(aud, "default", claims);
    }

    /**
     * @param aud      用户id
     * @param platform 用户平台，主要分web端、app端
     * @param claims
     * @return
     */
    public static String sign(Object aud, String platform, Map<String, Object> claims) {
        // issued at claim
        final long iat = System.currentTimeMillis() / 1000L;
        // expires claim. In this case the token expires in 60 * 60 seconds
        final long exp = iat + Long.valueOf(DefaultConfig.getTokenExp());
        final JWTSigner signer = new JWTSigner(DefaultConfig.getApiSecureKey());
        HashMap<String, Object> signClaims = Maps.newHashMap();
        signClaims.put(AUD, aud);
        signClaims.put(PLATFORM, platform);
        signClaims.put(EXP, exp);
        signClaims.put(IAT, iat);
        signClaims.putAll(claims);
        final String jwt = signer.sign(signClaims);
        return jwt;
    }

    public static Map<String, Object> verify(String jwt) throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        final JWTVerifier verifier = new JWTVerifier(DefaultConfig.getApiSecureKey());
        final Map<String, Object> claims = verifier.verify(jwt);
        return claims;
    }

}
