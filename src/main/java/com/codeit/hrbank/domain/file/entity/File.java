package com.codeit.hrbank.domain.file.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "file_type", nullable = false, length = 100)
    private String type;

    @Column(nullable = false)
    private Long size;

}
