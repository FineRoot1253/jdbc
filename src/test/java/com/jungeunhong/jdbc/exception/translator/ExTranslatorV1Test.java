package com.jungeunhong.jdbc.exception.translator;

import com.jungeunhong.jdbc.connection.ConnectionConst;
import com.jungeunhong.jdbc.member.domain.entity.Member;
import com.jungeunhong.jdbc.member.domain.exception.MyDBException;
import com.jungeunhong.jdbc.member.domain.exception.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLState;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.jungeunhong.jdbc.connection.ConnectionConst.*;

public class ExTranslatorV1Test {

    Repository repository;
    Service service;

    @BeforeEach
    void before(){
        repository = new Repository(new DriverManagerDataSource(URL, USERNAME, PASSWORD));
        service = new Service(repository);
    }

    @Slf4j
    @RequiredArgsConstructor
    static class Repository {

        private final DataSource dataSource;

        public Member save(Member member){
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection con = null;
            PreparedStatement pstmt =null;

            try{
                con = dataSource.getConnection();
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1,member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            }catch (SQLException e){
                if(PSQLState.UNIQUE_VIOLATION.getState().equals(e.getSQLState())){
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDBException(e);
            }finally {
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(con);
            }
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service {
        private final Repository repository;


        public void create(String memberId){

            try {
                log.info("saveId = {}",memberId);
                repository.save(new Member(memberId, 0));
            }catch (MyDuplicateKeyException e){
                log.info("키 중복, 복구 시도");
                String retryId = generateNewId(memberId);
                log.info("retry saveId = {}",retryId);
                repository.save(new Member(retryId, 0));
            }catch (MyDBException e){
                log.info("데이터 접근 예외 발생",e);
            }

        }

        private String generateNewId(String memberId){
            return memberId + new Random().nextInt(10000);
        }

    }

    @Test
    @DisplayName("duplicatedKeyTest:[success]")
    void duplicatedKeyTest(){
        //given

        // 중복 저장 시도
        service.create("myId");
        service.create("myId");


        //when

        //then

    }

}
