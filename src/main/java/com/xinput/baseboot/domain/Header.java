package com.xinput.baseboot.domain;

/**
 * http header中信息
 *
 * @author xinput
 * @date 2020-06-09 21:53
 */
public class Header {

  private String name;

  private String value;

  public Header(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "Header{" +
        "name='" + name + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
