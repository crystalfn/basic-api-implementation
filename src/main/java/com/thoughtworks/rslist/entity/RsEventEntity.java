package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rs_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String eventName;
    private String keywords;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
