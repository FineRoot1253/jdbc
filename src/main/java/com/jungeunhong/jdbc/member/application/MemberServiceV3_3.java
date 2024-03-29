package com.jungeunhong.jdbc.member.application;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import com.jungeunhong.jdbc.member.domain.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 memberRepositoryV3;

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV3.findByMemberId(fromId);
        Member toMember = memberRepositoryV3.findByMemberId(toId);

        memberRepositoryV3.update(fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepositoryV3.update(toId, toMember.getMoney()+ money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
