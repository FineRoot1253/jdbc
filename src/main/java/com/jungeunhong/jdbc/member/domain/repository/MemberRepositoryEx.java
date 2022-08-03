package com.jungeunhong.jdbc.member.domain.repository;

import com.jungeunhong.jdbc.member.domain.entity.Member;

import java.sql.SQLException;

/**
 * 체크 예외를 그래도 던지게 될 경우 인터페이스에 종속성이 생기게 된다는 예제이다.
 * 예외 전환을 통해 체크 예외 -> 언체크 예외 (런타임 예외)로 전환 해야 효용성이 있다.
 */
public interface MemberRepositoryEx {

    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}
