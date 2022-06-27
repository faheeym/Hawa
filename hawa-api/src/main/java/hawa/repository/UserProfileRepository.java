package hawa.repository;
import hawa.models.UserProfile;

public interface UserProfileRepository {

    UserProfile findByUsername(String username);
    UserProfile add(UserProfile userProfile);
    boolean update(UserProfile userProfile);
}
