package com.codeit.hrbank.domain.changelog.entity;

import lombok.Getter;

@Getter
public enum ChannelLogType {
  CREATED("직원 추가"),
  UPDATED("정보 수정"),
  DELETED("직원 삭제");

  private final String value;

  ChannelLogType(String value) {
    this.value = value;
  }
}
