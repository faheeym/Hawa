package hawa.repository;

import hawa.models.AppUser;
import hawa.models.Credentials;
import hawa.repository.mappers.AppUserMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findById(String username){
        final String sql = "select app_user_id from app_user where username=?;";

        try{
            int userId = jdbcTemplate.queryForObject(sql, Integer.class, username);
            return userId;
        }catch (DataAccessException ex){

            final String insertSql = "insert into app_user (username) " +
                    "values (?);";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, username);
                return ps;
            }, keyHolder);

            if (rowsAffected <= 0) {
                return -1;
            }

            return keyHolder.getKey().intValue();
        }
    }



    @Transactional
    public AppUser findByUsername(String username) {
        List<String> roles = getRolesByUsername(username);

        final String sql = """
                select
                    app_user_id,
                    first_name,
                    last_name,
                    email,
                    birthday,
                    gender,
                    relation_status,
                    profile_picture,
                    username,
                    password_hash,
                    disabled
                from app_user
                where username = ?;
                """;

        return jdbcTemplate.query(sql, new AppUserMapper(roles), username)
                .stream()
                .findFirst().orElse(null);
    }

    @Transactional
    public AppUser add(AppUser user) {

        final String sql = """
                insert into app_user ( username, password_hash, first_name, last_name, email, gender,
                relation_status, profile_picture, birthday) values (?,?,?,?,?,?,?,?,?);
                """;
        Credentials credentials = new Credentials();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirst_name());
            ps.setString(4,user.getLast_name());
            ps.setString(5,user.getEmail());
            ps.setString(6,user.getGender());
            ps.setString(7,user.getRelation_status());
            ps.setString(8,user.getProfile_picture());
            ps.setString(9, user.getBirthday());

            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setId(keyHolder.getKey().intValue());

        updateRoles(user);

        return user;
    }

    @Transactional
    public boolean update(AppUser user) {

        final String sql = """
                update app_user set
                    username = ?,
                    disabled = ?
                where app_user_id = ?;
                """;

        boolean updated = jdbcTemplate.update(sql,
                user.getUsername(), !user.isEnabled(), user.getId()) > 0;

        if (updated) {
            updateRoles(user);
        }

        return updated;
    }

    private void updateRoles(AppUser user) {
        // delete all roles, then re-create
        jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getId());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (String role : AppUser.convertAuthoritiesToRoles(authorities)) {
            String sql = """
                    insert into app_user_role (app_user_id, app_role_id)
                    select
                        ?,
                        app_role_id
                    from app_role where `name` = ?;
                    """;
            jdbcTemplate.update(sql, user.getId(), role);
        }
    }

    private List<String> getRolesByUsername(String username) {
        final String sql = """
                select
                    r.name
                from app_user_role ur
                inner join app_role r on ur.app_role_id = r.app_role_id
                inner join app_user au on ur.app_user_id = au.app_user_id
                where au.username = ?;
                """;
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), username);
    }
}