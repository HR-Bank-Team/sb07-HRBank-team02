package com.codeit.hrbank.domain.changelog.repository;

import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
@Slf4j
public class ChangeLogCustomRepositoryImpl implements ChangeLogCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ChangeLog> searchChangeLogs(
            String employeeNumber,
            String memo,
            String ipAddress,
            LocalDateTime atFrom,
            LocalDateTime atTo,
            String type,
            String cursor,
            String sortField,
            String sortDirection,
            Long idAfter,
            Integer limit) {
        log.info("searchChangeLogs");

        String sql = "SELECT * FROM change_logs c WHERE 1=1";

        if (employeeNumber != null) sql += " AND c.employee_number LIKE :employeeNumber";
        if (memo != null) sql += " AND c.memo LIKE :memo";
        if (ipAddress != null) sql += " AND c.ip_address LIKE :ipAddress ";
        if (atFrom != null) sql += " AND c.at >= :atFrom";
        if (atTo != null) sql += " AND c.at <= :atTo";
        if (type != null) sql += " AND c.type = :type";
        if (cursor != null) {
            if (sortField.equals("at")) {
                if (sortDirection.equals("asc")) sql += " AND (c.at > :cursor OR (c.at = :cursor AND c.id < :idAfter))";
                else sql += " AND (c.at < :cursor OR (c.at = :cursor AND c.id > :idAfter))";
            } else {
                if (sortDirection.equals("asc"))
                    sql += " AND (c.ip_address > :cursor OR (c.at = :cursor AND c.id < :idAfter))";
                else sql += " AND (c.ip_address < :cursor OR (c.at = :cursor AND c.id > :idAfter))";
            }
        }
        if (sortField.equals("at")) {
            if (sortDirection.equals("asc")) sql += " ORDER BY c.at ASC";
            else sql += " ORDER BY c.at DESC";
        } else {
            if (sortDirection.equals("asc")) sql += " ORDER BY c.ip_address ASC";
            else sql += " ORDER BY c.ip_address DESC";
        }

        Query query = em.createNativeQuery(sql, ChangeLog.class);

        if (employeeNumber != null) query.setParameter("employeeNumber", "%" + employeeNumber + "%");
        if (memo != null) query.setParameter("memo", "%" + memo + "%");
        if (ipAddress != null) query.setParameter("ipAddress", "%" + ipAddress + "%");
        if (atFrom != null) query.setParameter("atFrom", atFrom);
        if (atTo != null) query.setParameter("atTo", atTo);
        if (type != null) query.setParameter("type", type);
        if (cursor != null) {
            if (sortField.equals("at"))
                query.setParameter("cursor", Instant.parse(cursor).atZone(ZoneId.systemDefault()).toLocalDateTime());
            else query.setParameter("cursor", cursor);
            query.setParameter("idAfter", idAfter != null ? idAfter : 0L);
        }

        query.setMaxResults(limit);


        return query.getResultList();
    }

    @Override
    public Long countChangeLogs(
            String employeeNumber,
            String memo,
            String ipAddress,
            LocalDateTime atFrom,
            LocalDateTime atTo,
            String type
    ) {
        String sql = "SELECT COUNT(*) FROM change_logs c WHERE 1=1";

        if (employeeNumber != null) sql += " AND c.employee_number LIKE :employeeNumber";
        if (memo != null) sql += " AND c.memo LIKE :memo";
        if (ipAddress != null) sql += " AND c.ip_address LIKE :ipAddress";
        if (atFrom != null) sql += " AND c.at >= :atFrom";
        if (atTo != null) sql += " AND c.at <= :atTo";
        if (type != null) sql += " AND c.type = :type";

        Query query = em.createNativeQuery(sql);

        if (employeeNumber != null) query.setParameter("employeeNumber", "%" + employeeNumber + "%");
        if (memo != null) query.setParameter("memo", "%" + memo + "%");
        if (ipAddress != null) query.setParameter("ipAddress", "%" + ipAddress + "%");
        if (atFrom != null) query.setParameter("atFrom", atFrom);
        if (atTo != null) query.setParameter("atTo", atTo);
        if (type != null) query.setParameter("type", type);

        // COUNT 쿼리는 단일 값 반환
        Number countResult = (Number) query.getSingleResult();
        return countResult.longValue();
    }
}
