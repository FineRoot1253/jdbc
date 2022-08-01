package com.jungeunhong.jdbc.member.domain.repository;

import com.jungeunhong.jdbc.connection.DBConnectionUtils;
import com.jungeunhong.jdbc.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - ConnectionParam
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV2 {

    private final DataSource dataSource;

    public Member save(Member member) throws SQLException {

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

            log.error("db error", e);
            throw e;

        } finally {
            // 커넥션 닫기
            close(connection, preparedStatement, null);

        }

    }

    public Member findByMemberId(String memberId)throws SQLException {

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

            log.error("db error", e);
            throw e;

        } finally {
            // 커넥션 닫기
            close(connection, preparedStatement, rs);

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

            log.error("db error", e);
            throw e;

        } finally {
            // 커넥션 닫기
            close(preparedStatement, null);
        }
    }

    public void update(String memberId, int money) throws SQLException {

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

            log.error("db error", e);
            throw e;

        } finally {
            // 커넥션 닫기
            close(connection, preparedStatement, null);

        }

    }

    public void update(Connection conn,String memberId, int money) throws SQLException {

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

            log.error("db error", e);
            throw e;

        } finally {
            // 커넥션 닫기
            close(preparedStatement, null);

        }

    }

    public void delete(String memberId) throws SQLException {

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

            log.error("db error", e);
            throw e;

        } finally {
            // 커넥션 닫기
            close(connection, preparedStatement, null);

        }

    }

    private void close(Connection con, Statement statement, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(statement);
        JdbcUtils.closeConnection(con);
    }

    private void close(Statement statement, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(statement);
    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("get Connection={}, class={}",connection, connection.getClass());
        return connection;
    }
}
