package com.jungeunhong.jdbc.member.application;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import com.jungeunhong.jdbc.member.domain.repository.MemberRepositoryV2;
import com.jungeunhong.jdbc.member.domain.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static com.jungeunhong.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭 - 커넥션 파라미터 전달 방식 동기화
 */
@Slf4j
class MemberServiceV3_1Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV3 memberRepositoryV3;
    private MemberServiceV3_1 memberServiceV3_1;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV3 = new MemberRepositoryV3(dataSource);
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        memberServiceV3_1 = new MemberServiceV3_1(transactionManager, memberRepositoryV3);
    }

    @AfterEach
    void after()throws SQLException{
        memberRepositoryV3.delete(MEMBER_A);
        memberRepositoryV3.delete(MEMBER_B);
        memberRepositoryV3.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("successfulTrade:[success]")
    void successfulTrade() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberB);

        //when

        log.info("Start TX");
        memberServiceV3_1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(),2000);
        log.info("End TX");

        //then
        Member findMemberA = memberRepositoryV3.findByMemberId(memberA.getMemberId());
        Member findMemberB = memberRepositoryV3.findByMemberId(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);

    }

    @Test
    @DisplayName("failureTrade:[failure]")
    void failureTrade() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEX = new Member(MEMBER_EX, 10000);
        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberEX);

        //when
        assertThatThrownBy(()-> memberServiceV3_1.accountTransfer(memberA.getMemberId(), memberEX.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member findMemberA = memberRepositoryV3.findByMemberId(memberA.getMemberId());
        Member findMemberB = memberRepositoryV3.findByMemberId(memberEX.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);

    }

}