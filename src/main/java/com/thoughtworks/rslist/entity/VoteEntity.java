package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vote")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private int voteNumber;
    private LocalDateTime voteTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventEntity rsEventEntity;
}
