package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.ChannelEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChannelMapper {
    List<ChannelEntity> findAll();

    ChannelEntity findOneById(int id);

    int insert(ChannelEntity entity);

    int update(ChannelEntity entity);

    int deleteById(int id);

}
