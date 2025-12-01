package com.codeit.hrbank.domain.changelog.entity;

import com.codeit.hrbank.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diffs")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diff extends BaseEntity {

    @Column(name ="property_name", nullable = false, length = 100)
    private String propertyName;

    private String before;

    private String after;

    @ManyToOne
    @JoinColumn(name = "change_log_id", nullable = false)
    private ChangeLog changeLog;
}
