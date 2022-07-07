package com.jungeunhong.jdbc.member.domain.repository;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    @Test
    @DisplayName("crud:[Success]")
    void crud() throws SQLException {
        //given
        Member memberV0 = new Member("member_v1", 100000);
        int willChangeMoney = 2000000;

        //when
        repositoryV0.save(memberV0);
        Member foundMember = repositoryV0.findByMemberId(memberV0.getMemberId());
        //then
        assertThat(memberV0).isEqualTo(foundMember);


        //when
        repositoryV0.update(foundMember.getMemberId(),willChangeMoney);
        Member foundUpdatedMember = repositoryV0.findByMemberId(memberV0.getMemberId());
        //then
        assertThat(foundUpdatedMember.getMoney()).isEqualTo(2000000);

        //when
        repositoryV0.delete(foundUpdatedMember.getMemberId());
        //then
        assertThrows(NoSuchElementException.class, () -> repositoryV0.findByMemberId(memberV0.getMemberId()));


    }

}