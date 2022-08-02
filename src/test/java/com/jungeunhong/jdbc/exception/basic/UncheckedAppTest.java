package com.jungeunhong.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class UncheckedAppTest {

    @Test
    @DisplayName("checked_throw:[success]")
    void checked_throw(){
        //given
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("printEx:[success]")
    void printEx(){
        //given
        Controller controller = new Controller();
        try {
            controller.request();
        }catch (Exception e){
            log.info("ex",e);
        }
    }

    static class Controller{
        Service service = new Service();

        public void request(){
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

        public void callThrow(){
            repository.call();
            networkClient.call();
        }
    }

    static class Repository{
        /**
         * [예외전환]
         * 체크 예외인 SQLException을 캐치한 뒤
         * 언체크 예외인 RuntimeSQLException으로 던진다.
         */
        public void call(){
            try {
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException(e);
            }

        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient{
        public void call(){
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class RuntimeConnectException extends RuntimeException{

        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

}
