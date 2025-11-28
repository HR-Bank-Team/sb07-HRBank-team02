package com.codeit.hrbank.domain.changelog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "change_logs")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeLog {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "change_log_id")
  private Long id;

  @Column(nullable = false, length = 100)
  @Enumerated(EnumType.STRING)
  private ChannelLogType type;

  @Column(name = "ip_address", nullable = false)
  private String ipAddress;

  @Column(name = "at", nullable = false)
  private LocalDateTime at;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String memo;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "employee_number", nullable = false)
  private String employeeNumber;


}
