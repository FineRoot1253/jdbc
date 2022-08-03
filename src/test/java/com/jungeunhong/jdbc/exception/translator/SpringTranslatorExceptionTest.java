package com.jungeunhong.jdbc.exception.translator;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import com.jungeunhong.jdbc.member.domain.exception.MyDBException;
import com.jungeunhong.jdbc.member.domain.exception.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.jungeunhong.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SpringTranslatorExceptionTest {

    DataSource dataSource;

    @BeforeEach
    void before(){
        dataSource= new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Test
    @DisplayName("sqlExceptionErrorCode:[success]")
    void sqlExceptionErrorCode(){
        //given
        String sql = "select bad bad";
        // 중복 저장 시도
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeQuery();
        }catch (SQLException e){
            assertThat(e.getSQLState().equals(PSQLState.UNDEFINED_COLUMN.getState())).isTrue();
            String errorStateCode = e.getSQLState();
            log.info("errorStateCode={}",errorStateCode);
            log.info("error",e);
        }

    }

    @Test
    @DisplayName("exceptionTranslator:[success]")
    void exceptionTranslator(){
        //given
        String sql = "select bad bad";
        // 중복 저장 시도
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeQuery();
        }catch (SQLException e){
            assertThat(e.getSQLState().equals(PSQLState.UNDEFINED_COLUMN.getState())).isTrue();

            SQLErrorCodeSQLExceptionTranslator exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            DataAccessException dataAccessException = exceptionTranslator.translate("select", sql, e);

            log.info("error",dataAccessException);

            assertThat(dataAccessException).isNotNull();
            assertThat(dataAccessException.getClass()).isEqualTo(BadSqlGrammarException.class);

//            throw dataAccessException;

        }

    }

}
