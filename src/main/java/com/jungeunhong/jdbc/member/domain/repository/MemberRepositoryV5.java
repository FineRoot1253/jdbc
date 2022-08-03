package com.jungeunhong.jdbc.member.domain.repository;

import com.jungeunhong.jdbc.member.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * 체크 예외 -> 언체크 예외 (런타임 예외)로 변경
 * throws SQLException
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member){
        String sql = "insert into public.member(member_id, money) values (?,?);";
        template.update(sql,member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public Member findByMemberId(String memberId){
        String sql = "select * from public.member as m where m.member_id = ?;";
        return template.queryForObject(sql,memberRowMapper(), Member.class);
    }

    @Override
    public void update(String memberId, int money){
        String sql = "update public.member set money=? where member_id=?;";
        template.update(sql,money,money);
    }

    @Override
    public void delete(String memberId){
        String sql = "delete from public.member as m where m.member_id=?;";
        template.update(sql,memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
