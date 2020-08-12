package com.xinput.bootbase.domain;

import javax.validation.constraints.NotNull;

/**
 * @Author: xinput
 * @Date: 2020-06-15 15:23
 */
public class Token {

    private String userId;

    private String name;

    private String platform;

    private String token;

    @NotNull
    private String refreshToken;

    public Token() {
    }

    public Token(String userId, String token, @NotNull String refreshToken) {
        this.userId = userId;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public Token(String userId, String name, String token, @NotNull String refreshToken) {
        this.userId = userId;
        this.name = name;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public Token(String userId, String name, String platform, String token, @NotNull String refreshToken) {
        this.userId = userId;
        this.name = name;
        this.platform = platform;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
