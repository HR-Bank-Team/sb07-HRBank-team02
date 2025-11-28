package com.codeit.hrbank.domain.file.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "file_type", nullable = false)
    private String type;

    @Column(nullable = false)
    private Long size;

}
