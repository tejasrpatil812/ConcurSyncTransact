package com.example.ConcurSyncTransact.repo;

import com.example.ConcurSyncTransact.model.EntityLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityLockDao extends JpaRepository<EntityLock, Integer> {

    EntityLock findOneByEntityTypeIdAndEntityId(Integer id, Integer entityId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "INSERT INTO entity_locks (entity_type_id, entity_id, locked ) VALUES (?1, ?2, 0) ON DUPLICATE KEY UPDATE locked = locked")
    void insertOnDuplicateUpdate(Integer entityTypeId, Integer entityId);

}
