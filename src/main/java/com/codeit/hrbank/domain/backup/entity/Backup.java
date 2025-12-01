package com.codeit.hrbank.domain.backup.entity;

import com.codeit.hrbank.domain.base.BaseEntity;
import com.codeit.hrbank.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "backups")
@EntityListeners(AuditingEntityListener.class)
public class Backup extends BaseEntity {

    @Column(nullable = false, updatable = false, length = 100)
    private String worker; // IP 주소 저장

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt; // 백업 시작 시간

    @Column(name = "ended_at", nullable = false, updatable = false)
    private LocalDateTime endedAt; // 백업 종료 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 100)
    private Status status; // 백업 상태 (예: SUCCESS, FAILURE)

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "file_id",
            unique = true
    )
    private File file;
}
