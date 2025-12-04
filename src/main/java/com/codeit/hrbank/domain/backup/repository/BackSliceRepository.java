package com.codeit.hrbank.domain.backup.repository;

import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BackSliceRepository {


    @PersistenceContext
    EntityManager em;

    public List<Backup> getBackupSlice(
            String worker,
            BackupStatus status,
            LocalDateTime startedAtFrom,
            LocalDateTime startedAtTo,
            LocalDateTime cursor,
            Long idAfter,
            String sortField,
            String sortDirection,
            int size
    ) {
        String sql = "SELECT * FROM backups b WHERE 1=1";

        if (worker != null) sql += " AND b.worker LIKE :worker";
        if (status != null) sql += " AND b.status = :status";
        if (startedAtFrom != null) sql += " AND b.started_at >= :startedAtFrom";
        if (startedAtTo != null) sql += " AND b.started_at <= :startedAtTo";

        if (cursor != null) {
            if ("startedAt".equals(sortField)) {
                if ("asc".equals(sortDirection))
                    sql += " AND (b.started_at > :cursor OR (b.started_at = :cursor AND b.id > :idAfter))";
                else
                    sql += " AND (b.started_at < :cursor OR (b.started_at = :cursor AND b.id > :idAfter))";
            } else if ("endedAt".equals(sortField)) {
                if ("asc".equals(sortDirection))
                    sql += " AND (b.ended_at > :cursor OR (b.ended_at = :cursor AND b.id > :idAfter))";
                else
                    sql += " AND (b.ended_at < :cursor OR (b.ended_at = :cursor AND b.id > :idAfter))";
            }
        }

        if ("startedAt".equals(sortField)) sql += " ORDER BY b.started_at " + sortDirection;
        else if ("endedAt".equals(sortField)) sql += " ORDER BY b.ended_at " + sortDirection;

        sql += " LIMIT :size";

        Query query = em.createNativeQuery(sql, Backup.class);

        if (worker != null) query.setParameter("worker", "%" + worker + "%");
        if (status != null) query.setParameter("status", status.name());
        if (startedAtFrom != null) query.setParameter("startedAtFrom", startedAtFrom);
        if (startedAtTo != null) query.setParameter("startedAtTo", startedAtTo);
        if (cursor != null) query.setParameter("cursor", cursor);
        if (cursor != null) query.setParameter("idAfter", idAfter);
        query.setParameter("size", size);

        return query.getResultList();
    }

    public Long countBackup(
            String worker,
            BackupStatus status,
            LocalDateTime startedAtFrom,
            LocalDateTime startedAtTo,
            LocalDateTime cursor,
            Long idAfter,
            String sortField,
            String sortDirection
    ) {
        String sql = "SELECT COUNT(*) FROM backups b WHERE 1=1";

        if (worker != null) sql += " AND b.worker LIKE :worker";
        if (status != null) sql += " AND b.status = :status";
        if (startedAtFrom != null) sql += " AND b.started_at >= :startedAtFrom";
        if (startedAtTo != null) sql += " AND b.started_at <= :startedAtTo";

        if (cursor != null) {
            if ("startedAt".equals(sortField)) {
                if ("asc".equals(sortDirection))
                    sql += " AND (b.started_at > :cursor OR (b.started_at = :cursor AND b.id > :idAfter))";
                else
                    sql += " AND (b.started_at < :cursor OR (b.started_at = :cursor AND b.id > :idAfter))";
            } else if ("endedAt".equals(sortField)) {
                if ("asc".equals(sortDirection))
                    sql += " AND (b.ended_at > :cursor OR (b.ended_at = :cursor AND b.id > :idAfter))";
                else
                    sql += " AND (b.ended_at < :cursor OR (b.ended_at = :cursor AND b.id > :idAfter))";
            }
        }

        Query query = em.createNativeQuery(sql);

        if (worker != null) query.setParameter("worker", "%" + worker + "%");
        if (status != null) query.setParameter("status", status.name());
        if (startedAtFrom != null) query.setParameter("startedAtFrom", startedAtFrom);
        if (startedAtTo != null) query.setParameter("startedAtTo", startedAtTo);
        if (cursor != null) query.setParameter("cursor", cursor);
        if (cursor != null) query.setParameter("idAfter", idAfter);

        Number countResult = (Number) query.getSingleResult();
        return countResult.longValue();
    }

}
