package com.codeit.hrbank.domain.changelog.repository;

import com.codeit.hrbank.domain.changelog.entity.Diff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiffRepository extends JpaRepository<Diff, Long> {
}
