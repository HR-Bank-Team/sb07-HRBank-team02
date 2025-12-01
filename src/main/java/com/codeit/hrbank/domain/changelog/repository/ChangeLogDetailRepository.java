package com.codeit.hrbank.domain.changelog.repository;

import com.codeit.hrbank.domain.changelog.entity.ChangeLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogDetailRepository extends JpaRepository<ChangeLogDetail, Long> {
}
