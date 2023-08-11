package com.example.ConcurSyncTransact.helper;

public enum EntityType {
    PAYMENTS(1);
    private final Integer id;

    private EntityType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }
}
