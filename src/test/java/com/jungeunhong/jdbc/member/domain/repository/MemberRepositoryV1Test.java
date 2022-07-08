package com.jungeunhong.jdbc.member.domain.repository;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.jungeunhong.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;

    @BeforeEach
    void init(){
        // 기본 DriverManager - 항상 새로운 커넥션 획득
        DriverManagerDataSource dataSource1 = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // HikariCP - 커넥션 풀에서 커넥션 획득하는 방식
        HikariDataSource dataSource2 = new HikariDataSource();
        dataSource2.setJdbcUrl(URL);
        dataSource2.setUsername(USERNAME);
        dataSource2.setPassword(PASSWORD);
        dataSource2.setMaximumPoolSize(10);
        dataSource2.setPoolName("MyPool");

//        repositoryV1 = new MemberRepositoryV1(dataSource1);
        repositoryV1 = new MemberRepositoryV1(dataSource2);
    }

    @Test
    @DisplayName("crud:[Success]")
    void crud() throws SQLException,InterruptedException {
        //given
        Member memberV0 = new Member("member_v1", 100000);
        int willChangeMoney = 2000000;

        //when
        repositoryV1.save(memberV0);
        Member foundMember = repositoryV1.findByMemberId(memberV0.getMemberId());
        //then
        assertThat(memberV0).isEqualTo(foundMember);


        //when
        repositoryV1.update(foundMember.getMemberId(),willChangeMoney);
        Member foundUpdatedMember = repositoryV1.findByMemberId(memberV0.getMemberId());
        //then
        assertThat(foundUpdatedMember.getMoney()).isEqualTo(2000000);

        //when
        repositoryV1.delete(foundUpdatedMember.getMemberId());
        //then
        assertThrows(NoSuchElementException.class, () -> repositoryV1.findByMemberId(memberV0.getMemberId()));

        Thread.sleep(1000);
    }
}