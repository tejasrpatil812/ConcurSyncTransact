package com.example.ConcurSyncTransact.annotation;

import com.example.ConcurSyncTransact.helper.EntityType;

import java.lang.annotation.*;

@Target(value = { ElementType.METHOD })
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface LockEntity {

    EntityType type();

    int entityIdParamIndex();

}
