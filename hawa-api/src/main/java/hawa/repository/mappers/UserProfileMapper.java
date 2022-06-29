package hawa.repository.mappers;


import hawa.models.UserProfile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileMapper implements RowMapper<UserProfile> {
    @Override
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserProfile userProfile = new UserProfile();

        userProfile.setUser_profile_id(rs.getInt("user_profile_id"));
        userProfile.setUsername(rs.getString("username"));
        userProfile.setFirst_name(rs.getString("first_name"));
        userProfile.setLast_name(rs.getString("last_name"));
        userProfile.setEmail(rs.getString("email"));
        userProfile.setRelation_status(rs.getString("relation_status"));
        if (rs.getDate("birthday") != null) {
            userProfile.setBirthday(rs.getDate("birthday").toLocalDate());
        }
        userProfile.setProfile_picture(rs.getString("profile_picture"));
        userProfile.setGender(rs.getString("gender"));
        return userProfile;
    }
}
