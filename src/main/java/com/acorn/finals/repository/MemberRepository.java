package com.acorn.finals.repository;

import com.acorn.finals.model.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final SqlSession sqlSession;

    public List<MemberEntity> selectAll() {
        return sqlSession.selectList("member.selectAll");
    }

    public MemberEntity selectOne(MemberEntity memberEntity) {
        return sqlSession.selectOne("member.selectOne", memberEntity);
    }

    public int getNextSequence() {
        return sqlSession.selectOne("member.getNextSequence");
    }

    public int insert(MemberEntity memberEntity) {
        int id = getNextSequence();
        memberEntity.setId(id);
        sqlSession.insert("member.insert", memberEntity);
        return id;
    }

    public boolean update(MemberEntity memberEntity) {
        return sqlSession.update("member.update", memberEntity) > 0;
    }

    public boolean delete(MemberEntity memberEntity) {
        return sqlSession.delete("member.delete", memberEntity) > 0;
    }
}
