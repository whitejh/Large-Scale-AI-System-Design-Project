package com.team11.hub_service.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @CreatedBy
    @Column(updatable = false, length = 100)
    private String createdBy;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private  Timestamp updatedAt;

    @LastModifiedBy
    @Column(length = 100)
    private String updatedBy;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private  Timestamp deletedAt;

    @Column(length = 100)
    private String deletedBy;

    public void setDeleted(Timestamp deletedAt){
        this.deletedAt = deletedAt;
    }


//    public void setDeleted(Timestamp deletedAt, String deletedBy){
//        this.deletedAt = deletedAt;
//        this.deletedBy = deletedBy;
//    }
}