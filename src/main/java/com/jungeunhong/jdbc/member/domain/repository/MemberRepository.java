package com.jungeunhong.jdbc.member.domain.repository;

import com.jungeunhong.jdbc.member.domain.entity.Member;

public interface MemberRepository {
    Member save(Member member);
    Member findByMemberId(String memberId);
    void update(String memberId, int money);
    void delete(String memberId);
}
