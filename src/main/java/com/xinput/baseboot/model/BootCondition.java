package com.xinput.baseboot.model;

/**
 * 基础筛选条件
 *
 * @author xinput
 * @date 2020-06-20 12:22
 */
public class BootCondition {

  /**
   * 关键字查询
   */
  private String keywords;

  /**
   * 偏移量
   */
  private Integer offset;

  /**
   * 数据条数
   */
  private Integer limit;

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }
}
