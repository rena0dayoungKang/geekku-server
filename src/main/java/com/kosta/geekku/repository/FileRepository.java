package com.kosta.geekku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.CFile;

public interface FileRepository extends JpaRepository<CFile, Long> {
}
