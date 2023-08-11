package com.example.ConcurSyncTransact.service;


import com.example.ConcurSyncTransact.helper.EntityType;

public interface EntityLockService {

    void lockEntity(EntityType entityType, Integer entityId) throws Exception;

    void unLockEntity(EntityType entityType, Integer entityId);

}
