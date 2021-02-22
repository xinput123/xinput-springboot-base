package com.xinput.baseboot.domain;

/**
 * @author xinput
 * @date 2020-08-14 11:36
 */
public class JwtToken {

  private String aud;

  private String platform;

  /**
   * 当前时间戳，精确到秒
   */
  private Long iat;

  /**
   * 过期时间，值为 iat + DefaultConfig.getTokenExp()
   */
  private Long exp;

  public String getAud() {
    return aud;
  }

  public void setAud(String aud) {
    this.aud = aud;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public Long getIat() {
    return iat;
  }

  public void setIat(Long iat) {
    this.iat = iat;
  }

  public Long getExp() {
    return exp;
  }

  public void setExp(Long exp) {
    this.exp = exp;
  }
}
