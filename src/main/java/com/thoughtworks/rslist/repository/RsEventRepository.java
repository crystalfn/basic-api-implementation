package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RsEventRepository extends CrudRepository<RsEventEntity, Integer> {
    List<RsEventEntity> findAll();
}
