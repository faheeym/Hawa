package hawa.repository;

import hawa.models.UserProfile;
import hawa.repository.mappers.UserProfileMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserProfileJdbcTemplateRepository implements UserProfileRepository{
   private final JdbcTemplate jdbcTemplate;
   private final AppUserRepository appUserRepository;

    public UserProfileJdbcTemplateRepository(JdbcTemplate jdbcTemplate, AppUserRepository appUserRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    @Override
    public UserProfile findByUsername(String username) {
        final String sql= """
                select u.user_profile_id, u.first_name, u.last_name, u.gender, u.birthday, u.email, u.relation_status, u.profile_picture,
                a.username from user_profile u inner join app_user a where a.username= ?;
                """;
        UserProfile result = jdbcTemplate.query(sql, new UserProfileMapper(), username).stream()
                .findFirst().orElse(null);

        return result;
    }

    @Transactional
    @Override
    public UserProfile add(UserProfile userProfile) {
        userProfile.setApp_user_id(appUserRepository.findById(userProfile.getUsername()));
        if (userProfile.getApp_user_id()==-1){
            return null;
        }
        final String sql = """
                insert into user_profile(user_profile_id, first_name, last_name, birthday, email, relation_status, profile_picture, app_user_id, gender) values(?,?,?,?,?,?,?,?,?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection ->{
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userProfile.getUser_profile_id());
            ps.setString(2,userProfile.getFirst_name());
            ps.setString(3, userProfile.getLast_name());
            ps.setDate(4,userProfile.getBirthday()== null ? null : Date.valueOf(userProfile.getBirthday()));
            ps.setString(5, userProfile.getEmail());
            ps.setString(6, userProfile.getRelation_status());
            ps.setString(7, userProfile.getProfile_picture());
            ps.setInt(8, userProfile.getApp_user_id());
            ps.setString(9,userProfile.getGender());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0){
            return null;
        }

        userProfile.setUser_profile_id(keyHolder.getKey().intValue());
        return userProfile;
    }

    @Override
    public boolean update(UserProfile userProfile) {
        userProfile.setApp_user_id(appUserRepository.findById(userProfile.getUsername()));

        if(userProfile.getApp_user_id()==-1){
            return false;
        }
        final String sql= """
                update user_profile set
                app_user_id = ?,
                first_name= ?,
                last_name= ?,
                profile_picture = ?,
                relation_status = ?,
                birthday = ?,
                email = ?,
                gender= ?
                where user_profile_id =?;
                """;

        return jdbcTemplate.update(sql,
                userProfile.getApp_user_id(),
                userProfile.getFirst_name(),
                userProfile.getLast_name(),
                userProfile.getProfile_picture(),
                userProfile.getRelation_status(),
                userProfile.getBirthday(),
                userProfile.getEmail(),
                userProfile.getGender(),
                userProfile.getUser_profile_id())>0;
    }
}
