package com.example.ConcurSyncTransact.interceptor;

import com.example.ConcurSyncTransact.annotation.LockEntity;
import com.example.ConcurSyncTransact.helper.EntityType;
import com.example.ConcurSyncTransact.service.EntityLockService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class EntityLockInterceptor {

    private static Logger     logger = LoggerFactory.getLogger(EntityLockInterceptor.class);

    @Autowired
    private EntityLockService entityLockService;

    @Around("@annotation(com.example.ConcurSyncTransact.annotation.LockEntity)")
    public Object lockEntity(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("LOCKING ENTITY");
        Method interfaceMethod = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Method method = proceedingJoinPoint.getTarget().getClass()
                .getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
        LockEntity lockEntity = method.getAnnotation(LockEntity.class);
        int entityIdParamIndex = lockEntity.entityIdParamIndex();
        Object[] params = proceedingJoinPoint.getArgs();
        if (params.length <= entityIdParamIndex) {
            throw new Exception("EntityIdParam Index not found");
        }
        Object entityIdParamObject = params[entityIdParamIndex];
        if (!(entityIdParamObject instanceof Integer)) {
            throw new Exception("EntityIdParamIndex argument should be integer");
        }
        Integer entityId = (Integer) entityIdParamObject;
        EntityType entityType = lockEntity.type();
        logger.info("LOCKING ENTITY {} WITH ID {}", entityType, entityId);
        entityLockService.lockEntity(entityType, entityId);
        logger.info("LOCKED ENTITY {} WITH ID {}", entityType, entityId);
        try {
            return proceedingJoinPoint.proceed();
        }
        finally {
            logger.info("UNLOCKING ENTITY {} WITH ID {}", entityType, entityId);
            entityLockService.unLockEntity(entityType, entityId);
            logger.info("UNLOCKED ENTITY {} WITH ID {}", entityType, entityId);
        }
    }

}
