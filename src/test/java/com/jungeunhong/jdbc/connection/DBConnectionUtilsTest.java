package com.jungeunhong.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.jungeunhong.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DBConnectionUtilsTest {

    @Test
    @DisplayName("connectionTest:[Success]")
    void connectionTest() {
        //given
        Connection connection = DBConnectionUtils.getConnection();
        //when

        //then
        assertThat(connection).isNotNull();
    }

    @Test
    @DisplayName("driverManager:[Success]")
    void driverManager() throws SQLException {
        //given
        Connection conn1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection conn2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        //when

        //then
        log.info("connection={} class={}", conn1, conn1.getClass());
        log.info("connection={} class={}", conn2, conn2.getClass());
    }

    @Test
    @DisplayName("dataSourceDriverManager:[Success]")
    void dataSourceDriverManager() throws SQLException {
        //given
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //when
        userDataSource(dataSource);
        //then

    }

    @Test
    @DisplayName("dataSourceConnectionPool:[Success]")
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //given
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        //when
        userDataSource(dataSource);
        Thread.sleep(1000);


        //then

    }

    private void userDataSource(DataSource dataSource) throws SQLException {
        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();
//        Connection conn3 = dataSource.getConnection();
//        Connection conn4 = dataSource.getConnection();
//        Connection conn5 = dataSource.getConnection();
//        Connection conn6 = dataSource.getConnection();
//        Connection conn7 = dataSource.getConnection();
//        Connection conn8 = dataSource.getConnection();
//        Connection conn9 = dataSource.getConnection();
//        Connection conn10 = dataSource.getConnection();
//        Connection conn11 = dataSource.getConnection();
//        Connection conn12 = dataSource.getConnection();
//        Connection conn13 = dataSource.getConnection();
//        Connection conn14 = dataSource.getConnection();

        log.info("connection={} class={}", conn1, conn1.getClass());
        log.info("connection={} class={}", conn2, conn2.getClass());
    }

}