package com.kosta.geekku.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String directory;
    private String name;
    private String contentType;
    private Long size;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;
}
