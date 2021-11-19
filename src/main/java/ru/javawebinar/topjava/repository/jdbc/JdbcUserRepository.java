package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final String baseUserSelectSql = """
            SELECT u.*, ur.role as roles FROM users u
            LEFT JOIN user_roles ur on u.id = ur.user_id
            """;

    private final ResultSetExtractor<List<User>> resultSetExtractor = rs -> {
        Map<Integer, User> userMap = new HashMap<>();

        while (rs.next()) {
            User user = ROW_MAPPER.mapRow(rs, rs.getRow());
            if (user.getRoles() == null) {
                user.setRoles(Collections.emptyList());
            }
            userMap.merge(user.getId(), user, (mappedUser, newUser) -> {
                mappedUser.getRoles().addAll(newUser.getRoles());
                return mappedUser;
            });
        }
        return List.copyOf(userMap.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
        }
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        String rolesInsertSql = "INSERT INTO user_roles (user_id, role) values (:user_id, :role)";

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            List<MapSqlParameterSource> rolesParams = extracted(user.getRoles(), newKey.intValue());
            namedParameterJdbcTemplate.batchUpdate(rolesInsertSql, rolesParams.toArray(MapSqlParameterSource[]::new));
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            } else {
                namedParameterJdbcTemplate.update("DELETE FROM user_roles WHERE user_id=:id", parameterSource);
                if (user.getRoles().size() > 0) {
                    List<MapSqlParameterSource> rolesParams = extracted(user.getRoles(), user.getId());
                    namedParameterJdbcTemplate.batchUpdate(rolesInsertSql, rolesParams.toArray(MapSqlParameterSource[]::new));
                }
            }
        }
        return user;
    }

    private List<MapSqlParameterSource> extracted(Set<Role> userRoles, int userId) {
        List<MapSqlParameterSource> params = new ArrayList<>();

        for (Role role : userRoles) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("user_id", userId);
            source.addValue("role", role.toString());
            params.add(source);
        }
        return params;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(baseUserSelectSql + " WHERE id=?", resultSetExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query(baseUserSelectSql + " WHERE email=?", resultSetExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(baseUserSelectSql + " ORDER BY name, email", resultSetExtractor);
    }
}
