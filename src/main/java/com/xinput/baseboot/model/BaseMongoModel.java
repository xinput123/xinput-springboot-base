package com.xinput.baseboot.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class BaseMongoModel {

  @Id
  private String id;

  private LocalDateTime createAt = LocalDateTime.now();

  private LocalDateTime updateAt = LocalDateTime.now();

  private Integer recordState = 0;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getCreateAt() {
    return createAt;
  }

  public void setCreateAt(LocalDateTime createAt) {
    this.createAt = createAt;
  }

  public LocalDateTime getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(LocalDateTime updateAt) {
    this.updateAt = updateAt;
  }

  public Integer getRecordState() {
    return recordState;
  }

  public void setRecordState(Integer recordState) {
    this.recordState = recordState;
  }
}
