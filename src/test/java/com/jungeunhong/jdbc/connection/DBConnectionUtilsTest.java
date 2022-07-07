package com.jungeunhong.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DBConnectionUtilsTest {

    @Test
    @DisplayName("connectionTest:[Success]")
    void connectionTest(){
        //given
        Connection connection = DBConnectionUtils.getConnection();
        //when

        //then
        assertThat(connection).isNotNull();
    }

}