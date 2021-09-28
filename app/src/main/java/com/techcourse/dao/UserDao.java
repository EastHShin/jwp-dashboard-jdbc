package com.techcourse.dao;

import com.techcourse.domain.User;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);
    private static final String QUERY_SQL = "query : {}";

    private static final String ID = "id";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";

    private final JdbcTemplate<User> jdbcTemplate;

    public UserDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate<>(dataSource);
    }

    public void insert(User user) {
        String sql = "insert into users (account, password, email) values (?, ?, ?)";
        log.debug(QUERY_SQL, sql);
        jdbcTemplate.update(sql, user.getAccount(), user.getPassword(), user.getEmail());
    }

    public void update(User user) {
        String sql = "update users set account=?, password=?, email=? where id = ?";
        log.debug(QUERY_SQL, sql);
        jdbcTemplate.update(sql, user.getAccount(), user.getPassword(), user.getEmail(), user.getId());
    }

    public List<User> findAll() {
        String sql = "select * from users";
        log.debug(QUERY_SQL, sql);
        return jdbcTemplate.query(sql, getUserRowMapper());
    }

    public Optional<User> findById(Long id) {
        String sql = "select id, account, password, email from users where id = ?";
        log.debug(QUERY_SQL, sql);
        return jdbcTemplate.queryForObject(sql, getUserRowMapper(), id);
    }

    public Optional<User> findByAccount(String account) {
        String sql = "select * from users where account = ?";
        log.debug(QUERY_SQL, sql);
        return jdbcTemplate.queryForObject(sql, getUserRowMapper(), account);
    }

    private RowMapper<User> getUserRowMapper() {
        return rs -> new User(
                rs.getLong(ID),
                rs.getString(ACCOUNT),
                rs.getString(PASSWORD),
                rs.getString(EMAIL));
    }
}
