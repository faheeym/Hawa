package hawa.repository;

import hawa.models.AppUser;

public interface AppUserRepository {
    int findById(String username);
    AppUser findByUsername(String username);
    AppUser add(AppUser user);
    boolean update(AppUser user);
}
