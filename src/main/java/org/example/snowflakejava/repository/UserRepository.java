package org.example.snowflakejava.repository;

import org.example.snowflakejava.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @Qualifier("snowflakeJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sql = "SELECT id, login FROM user";
        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(User.class));
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT id, login FROM user WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{id},
                new BeanPropertyRowMapper<>(User.class));
        return users.stream().findFirst();
    }

    public User save(User user) {
        if (user.getId() == null) {
            String insertSql = "INSERT INTO user (id, login) VALUES (?, ?)";
            Long newId = System.currentTimeMillis();
            jdbcTemplate.update(insertSql, newId, user.getLogin());
            user.setId(newId);
        } else {
            String updateSql = "UPDATE user SET login = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, user.getLogin(), user.getId());
        }
        return user;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
