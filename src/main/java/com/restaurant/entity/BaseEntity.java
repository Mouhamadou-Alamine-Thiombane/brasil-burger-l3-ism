package com.restaurant.entity;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean archived;
    
    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.archived = false;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isArchived() {
        return archived;
    }
    
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}