package com.jungeunhong.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {

    @Test
    @DisplayName("checked_catch:[success]")
    void checked_catch(){
        //given
        Service service = new Service();
        service.callCatch();
    }
    @Test
    @DisplayName("checked_throw:[success]")
    void checked_throw(){
        //given
        Service service = new Service();
        Assertions.assertThatThrownBy(service::callThrow).isInstanceOf(MyUncheckedException.class);
    }

    /**
     * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException{

        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * Unchecked 예외는
     * 예외를 잡거나 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다.
     */
    static class Service{
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다.
         */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                log.info("예외 처리, message={}",e.getMessage(), e);
            }
        }

        /**
         * 언체크 예외를 밖으로 던지는 코드
         * 언체크 예외는 throws를 생략해도 자동으로 상위로 던져진다.
         * 체크 예외와는 다르게 생략 가능하다.
         * @throws MyUncheckedException
         */
        public void callThrow(){
            repository.call();
        }
    }

    static class Repository{
        public void call(){
            throw new MyUncheckedException("ex");
        }
    }
}
