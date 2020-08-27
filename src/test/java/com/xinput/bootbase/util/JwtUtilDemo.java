package com.xinput.bootbase.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.domain.JwtToken;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author: xinput
 * @Date: 2020-08-12 17:15
 */
public class JwtUtilDemo {
//    @Test
//    public void test() throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException {
//        String token = JwtUtils.sign("123");
//        System.out.println("token : " + token);
//
////        Map<String, Object> map = JwtUtils.verify(token);
////        System.out.println("token 解析结果：");
////        map.forEach((k, v) -> {
////            System.out.println(k + " = " + v);
////        });
//    }
//
//    @Test
//    public void test02() throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("name", "xinput");
//
//        String token = sign("1001", "web", claims);
//        System.out.println("token : " + token);
//
//        Map<String, Object> map = verify(token);
//        System.out.println("token 解析结果：");
//        map.forEach((k, v) -> {
//            System.out.println(k + " = " + v);
//        });
//    }
//
//    /**
//     * @param aud      用户id
//     * @param platform 用户平台，主要分web端、app端
//     * @param claims
//     * @return
//     */
//    public static String sign(Object aud, String platform, Map<String, Object> claims) {
//        // issued at claim
//        final long iat = System.currentTimeMillis() / 1000L;
//        // expires claim. In this case the token expires in 60 * 60 seconds
//        final long exp = iat + Long.valueOf(DefaultConfig.getTokenExp());
//        final JWTSigner signer = new JWTSigner(DefaultConfig.getApiSecureKey());
//        HashMap<String, Object> signClaims = new HashMap<>();
//        signClaims.put("aud", aud);
//        signClaims.put("platform", platform);
//        signClaims.put("exp", exp);
//        signClaims.put("iat", iat);
//        signClaims.putAll(claims);
//        final String jwt = signer.sign(signClaims);
//        return jwt;
//    }
//
//    public static Map<String, Object> verify(String jwt) throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
//        final JWTVerifier verifier = new JWTVerifier(DefaultConfig.getApiSecureKey());
////        final JWTVerifier verifier = new JWTVerifier("xinput");
//        final Map<String, Object> claims = verifier.verify(jwt);
//        return claims;
//    }
//
//    @Test
//    public void jwtToken() throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException {
//        String token = JwtUtils.sign("123567");
//        JwtToken jwtToken = JwtUtils.verifyJwtToken(token);
//        System.out.println(JsonUtils.toJsonString(jwtToken, true));
//
//        JwtUtils.verifyJwtToken("");
//    }

    @Test
    public void createToken() {
        // 设置一个私钥，也可以使用KeyProvider产生，参见：
        // @link https://github.com/auth0/java-jwt#using-a-keyprovider
        String key = "Shenpibaipao";
        // 给定一个算法，如HmacSHA-256
        Algorithm alg = Algorithm.HMAC256(key);

        // 1 签发Token
        Date currentTime = new Date();
        String token = JWT.create()
                .withIssuer("CSDN Blog") // 发行者
                .withSubject("userid") // 用户身份标识
                .withAudience("CSDN User") // 用户单位
                .withIssuedAt(currentTime) // 签发时间
                .withExpiresAt(new Date(currentTime.getTime() + 24 * 3600 * 1000L)) // 一天有效期
                .withJWTId("001") // 分配JWT的ID
                .withClaim("uid", "1001") // 定义公共域信息
                .sign(alg);

        System.out.println("生成的Token是:" + token);

        // 2 验证Token
        JWTVerifier verifier = JWT.require(alg)
                .build();

        DecodedJWT originToken = null;
        try {
            originToken = verifier.verify(token);
            System.out.println("验证通过!");
        } catch (TokenExpiredException e) {
            System.out.println("过期");
            e.printStackTrace();
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            System.out.println("验证失败!");
        }

        // 3 尝试解码
        System.out.println("解码得到用户是：" + originToken.getAudience());
        System.out.println("解码得到发行者是:" + originToken.getIssuer());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("解码得到签发时间是:" + sdf.format(originToken.getIssuedAt()));
        System.out.println("解码得到公共域信息是:" + originToken.getClaim("uid").asString());
    }

    @Test
    public void create() {
        String token = JwtUtils.sign("1001");
        System.out.println("token : " + token);

        final Algorithm ALGORITHM = Algorithm.HMAC256(DefaultConfig.getApiSecureKey());
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        DecodedJWT originToken = verifier.verify(token);
        Map<String, Claim> claimMaps = originToken.getClaims();

        claimMaps.forEach((k, v) -> {
            System.out.println(k + " = " + v.toString());
        });
        Claim claim = claimMaps.get("iat");
        System.out.println("iat = " + claim.asLong());
        System.out.println("iat = " + claim.asInt());
        System.out.println("iat = " + DateUtils.format(claim.asDate(), DateUtils.DATE_TIME_FORMATTER_STRING));
        System.out.println(DateUtils.format(originToken.getExpiresAt(), DateUtils.DATE_TIME_FORMATTER_STRING));

        Claim expClaim = claimMaps.get("exp");
        System.out.println("exp = " + expClaim.asLong());
        System.out.println("exp = " + expClaim.asInt());
        System.out.println("exp = " + DateUtils.format(expClaim.asDate(), DateUtils.DATE_TIME_FORMATTER_STRING));
        System.out.println(DateUtils.format(originToken.getExpiresAt(), DateUtils.DATE_TIME_FORMATTER_STRING));

    }

    @Test
    public void test02() {
        String token = JwtUtils.sign("100001");
        System.out.println(token);

        try {
            JwtToken jwtToken = JwtUtils.verifyJwtToken(token);
            System.out.println(JsonUtils.toJsonString(jwtToken, true));
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
