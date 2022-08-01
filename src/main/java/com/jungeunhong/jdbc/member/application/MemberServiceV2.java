package com.jungeunhong.jdbc.member.application;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import com.jungeunhong.jdbc.member.domain.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepositoryV2;

    public void accountTransfer(String fromId, String toId, int money)throws SQLException{
        Connection connection = dataSource.getConnection();

        try {
            connection.setAutoCommit(false);

            bizLogic(fromId, toId, money, connection);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw new IllegalStateException(e);
        }finally {
            release(connection);
        }
    }

    private void bizLogic(String fromId, String toId, int money, Connection connection) throws SQLException {
        Member fromMember = memberRepositoryV2.findByMemberId(connection, fromId);
        Member toMember = memberRepositoryV2.findByMemberId(connection, toId);

        memberRepositoryV2.update(connection, fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepositoryV2.update(connection, toId, toMember.getMoney()+ money);
    }

    private void release(Connection connection) {
        if(connection != null){
            try {
                connection.setAutoCommit(true); // 원래 디폴트 상태로 만들어주고 커넥션 풀에 돌려줘야 나중에 가져다 쓸때 혼동이 없다!!
                connection.close();
            }catch (Exception e){
                log.info("error ", e);
            }
        }
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
