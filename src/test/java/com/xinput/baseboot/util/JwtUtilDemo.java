package com.xinput.baseboot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xinput.baseboot.config.BaseBootConfig;
import com.xinput.baseboot.domain.JwtToken;
import com.xinput.bleach.util.DateUtils;
import com.xinput.bleach.util.JsonUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author xinput
 * @date 2020-08-12 17:15
 */
public class JwtUtilDemo {

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

        final Algorithm ALGORITHM = Algorithm.HMAC256(BaseBootConfig.getApiSecureKey());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
