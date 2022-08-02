package com.jungeunhong.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class CheckedAppTest {

    @Test
    @DisplayName("checked_throw:[success]")
    void checked_throw(){
        //given
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(controller::reqeust).isInstanceOf(Exception.class);
    }

    static class Controller{
        Service service = new Service();

        public void reqeust() throws SQLException, ConnectException {
            service.callThrow();
        }
    }

    /**
     * Checked 예외는
     * 예외를 잡아서 처리하거나, 던지거나 둘중 하나 택일 해야한다.
     */
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void callThrow() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }
    }

    static class Repository{
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient{
        public void call() throws ConnectException {
            throw new ConnectException();
        }
    }
}
