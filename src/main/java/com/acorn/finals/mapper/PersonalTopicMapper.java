package com.acorn.finals.mapper;

import com.acorn.finals.model.entity.PersonalTopicEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonalTopicMapper {

    List<PersonalTopicEntity> findAll();

    PersonalTopicEntity findOneById(int id);

    List<PersonalTopicEntity> findAllByMemberId(int member1Id);

    List<PersonalTopicEntity> findOneByMember1IdAndMember2Id();

    int insert(PersonalTopicEntity entity);

    int update(PersonalTopicEntity entity);

    int deleteById(int id);

}
