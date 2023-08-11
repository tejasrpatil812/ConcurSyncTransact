package com.example.ConcurSyncTransact.service.impl;

import com.example.ConcurSyncTransact.helper.EntityType;
import com.example.ConcurSyncTransact.model.EntityLock;
import com.example.ConcurSyncTransact.repo.EntityLockDao;
import com.example.ConcurSyncTransact.service.EntityLockService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.util.Assert;

@Service
public class EntityLockServiceImpl implements EntityLockService {

    @Autowired
    private EntityLockDao entityLockDao;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void lockEntity(EntityType entityType, Integer entityId) throws Exception {
        Assert.notNull(entityType, "EntityType cannot be null");
        Assert.notNull(entityId, "entityId cannot be null");
        Integer entityTypeId = entityType.getId();
        EntityLock entityLock = entityLockDao.findOneByEntityTypeIdAndEntityId(entityTypeId, entityId);
        if (entityLock != null && entityLock.getLocked()) {
            throw new Exception(
                    String.format("Entity %s with id %d is already getting modified", entityType.name(), entityId));
        }
        if (entityLock == null) {
            entityLockDao.insertOnDuplicateUpdate(entityTypeId, entityId);
        }
        EntityLock savedEntityLock = entityLockDao.findOneByEntityTypeIdAndEntityId(entityTypeId, entityId);
        if (savedEntityLock.getLocked()) {
            throw new Exception(
                    String.format(
                            "Entity %s with id %d is already getting modified and thus locked",
                            entityType.name(),
                            entityId));
        }
        savedEntityLock.setLocked(Boolean.TRUE);
        entityLockDao.save(savedEntityLock);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void unLockEntity(EntityType entityType, Integer entityId) {
        Assert.notNull(entityType, "EntityType cannot be null");
        Assert.notNull(entityId, "entityId cannot be null");
        Integer entityTypeId = entityType.getId();
        EntityLock entityLock = entityLockDao.findOneByEntityTypeIdAndEntityId(entityTypeId, entityId);
        entityLock.setLocked(Boolean.FALSE);
        entityLockDao.save(entityLock);
    }

}
