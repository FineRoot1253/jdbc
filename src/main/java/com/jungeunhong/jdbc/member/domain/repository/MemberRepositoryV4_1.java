package com.jungeunhong.jdbc.member.domain.repository;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import com.jungeunhong.jdbc.member.domain.exception.MyDBException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * 체크 예외 -> 언체크 예외 (런타임 예외)로 변경
 * throws SQLException
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV4_1 implements MemberRepository{

    private final DataSource dataSource;

    @Override
    public Member save(Member member){

        String sql = "insert into public.member(member_id, money) values (?,?);";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Connection 객체, Sql 객체 초기화
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // SQL 객체 파라미터 파인딩
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());

            // SQL 실행
            int effectedRowCount = preparedStatement.executeUpdate();

            // 결과 레코드 리턴
            return member;

        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            // 커넥션 닫기
            close(connection, preparedStatement, null);

        }

    }

    @Override
    public Member findByMemberId(String memberId){

        String sql = "select * from public.member as m where m.member_id = ?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            // Connection 객체, Sql 객체 초기화
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // SQL 객체 파라미터 파인딩
            preparedStatement.setString(1, memberId);

            // SQL 실행
            rs = preparedStatement.executeQuery();

            // 리턴 레코드 세팅
            Member member = new Member();

            // 결과 레코드 리턴
            if(rs.next()){
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found" + memberId);
            }


        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            // 커넥션 닫기
            close(connection,preparedStatement, rs);

        }
    }

    public Member findByMemberId(Connection conn,String memberId)throws SQLException {

        String sql = "select * from public.member as m where m.member_id = ?;";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            // Connection 객체, Sql 객체 초기화
            preparedStatement = conn.prepareStatement(sql);

            // SQL 객체 파라미터 파인딩
            preparedStatement.setString(1, memberId);

            // SQL 실행
            rs = preparedStatement.executeQuery();

            // 리턴 레코드 세팅
            Member member = new Member();

            // 결과 레코드 리턴
            if(rs.next()){
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found" + memberId);
            }


        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            // 커넥션 닫기
            close(conn, preparedStatement, null);
        }
    }

    @Override
    public void update(String memberId, int money){

        String sql = "update public.member set money=? where member_id=?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Connection 객체, Sql 객체 초기화
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // SQL 객체 파라미터 파인딩
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);

            // SQL 실행
            int effectedRowCount = preparedStatement.executeUpdate();
            log.info("resultSize={}",effectedRowCount);

        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            // 커넥션 닫기
            close(connection, preparedStatement, null);

        }

    }

    public void update(Connection conn,String memberId, int money){

        String sql = "update public.member set money=? where member_id=?;";
        PreparedStatement preparedStatement = null;

        try {
            // Connection 객체, Sql 객체 초기화
            preparedStatement = conn.prepareStatement(sql);

            // SQL 객체 파라미터 파인딩
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);

            // SQL 실행
            int effectedRowCount = preparedStatement.executeUpdate();
            log.info("resultSize={}",effectedRowCount);

        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            // 커넥션 닫기
            close(conn, preparedStatement, null);

        }

    }

    @Override
    public void delete(String memberId){

        String sql = "delete from public.member as m where m.member_id=?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Connection 객체, Sql 객체 초기화
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // SQL 객체 파라미터 파인딩
            preparedStatement.setString(1, memberId);

            // SQL 실행
            int effectedRowCount = preparedStatement.executeUpdate();

            log.info("resultSize={}",effectedRowCount);

        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            // 커넥션 닫기
            close(connection, preparedStatement, null);

        }

    }

    private void close(Connection con, Statement statement, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(statement);
        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private void close(Statement statement, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(statement);
    }

    private Connection getConnection() throws SQLException {
        //주의! 트랜잭션 동기
        Connection conn = DataSourceUtils.getConnection(dataSource);
        log.info("get Connection={}, class={}",conn, conn.getClass());
        return conn;
    }
}
