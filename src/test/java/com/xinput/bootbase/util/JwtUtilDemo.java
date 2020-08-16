package com.xinput.bootbase.util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.domain.JwtToken;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xinput
 * @Date: 2020-08-12 17:15
 */
public class JwtUtilDemo {
    @Test
    public void test() throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException {
        String token = JwtUtils.sign("123");
        System.out.println("token : " + token);

//        Map<String, Object> map = JwtUtils.verify(token);
//        System.out.println("token 解析结果：");
//        map.forEach((k, v) -> {
//            System.out.println(k + " = " + v);
//        });
    }

    @Test
    public void test02() throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "xinput");

        String token = sign("1001", "web", claims);
        System.out.println("token : " + token);

        Map<String, Object> map = verify(token);
        System.out.println("token 解析结果：");
        map.forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
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
        HashMap<String, Object> signClaims = new HashMap<>();
        signClaims.put("aud", aud);
        signClaims.put("platform", platform);
        signClaims.put("exp", exp);
        signClaims.put("iat", iat);
        signClaims.putAll(claims);
        final String jwt = signer.sign(signClaims);
        return jwt;
    }

    public static Map<String, Object> verify(String jwt) throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        final JWTVerifier verifier = new JWTVerifier(DefaultConfig.getApiSecureKey());
//        final JWTVerifier verifier = new JWTVerifier("xinput");
        final Map<String, Object> claims = verifier.verify(jwt);
        return claims;
    }

    @Test
    public void jwtToken() throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException {
        String token = JwtUtils.sign("123567");
        JwtToken jwtToken = JwtUtils.verifyJwtToken(token);
        System.out.println(JsonUtils.toJsonString(jwtToken, true));

        JwtUtils.verifyJwtToken("");
    }

}
