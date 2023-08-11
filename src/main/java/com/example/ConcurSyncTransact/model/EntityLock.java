package com.example.ConcurSyncTransact.model;

import jakarta.persistence.*;

import java.util.Optional;

/**
 *
 * @author Saif Ali Khan
 *
 */
@Entity
@Table(name = "entity_locks")
public class EntityLock{

    private static final long serialVersionUID = -2994141459140030449L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer           id;

    @Column(name = "entity_type_id")
    private Integer           entityTypeId;

    @Column(name = "entity_id")
    private Integer           entityId;

    @Column(name = "locked")
    private Boolean           locked;

    @PrePersist
    public void onCreation() {
        locked = Optional.ofNullable(locked).orElse(Boolean.FALSE);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Integer entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

}
