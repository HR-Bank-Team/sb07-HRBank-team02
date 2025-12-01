package com.codeit.hrbank.domain.file.entity;

import com.codeit.hrbank.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "files")
public class File extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "file_type", nullable = false, length = 100)
    private String type;

    @Column(nullable = false)
    private Long size;

}
