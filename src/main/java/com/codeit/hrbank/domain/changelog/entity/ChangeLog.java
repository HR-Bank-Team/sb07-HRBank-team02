package com.codeit.hrbank.domain.changelog.entity;

import com.codeit.hrbank.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.Table;
import java.time.LocalDateTime;

import lombok.*;


@Entity
@Table(name = "change_logs")
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeLog extends BaseEntity {

  @Column(nullable = false, length = 100)
  @Enumerated(EnumType.STRING)
  private ChangeLogType type;

  @Column(name = "ip_address", nullable = false)
  private String ipAddress;

  @Column(nullable = false)
  private LocalDateTime at;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String memo;

  @Column(name = "employee_number", nullable = false)
  private String employeeNumber;


}
