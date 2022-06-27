package hawa.security;

import hawa.domain.Result;
import hawa.domain.ResultType;
import hawa.models.AppUser;
import hawa.models.Credentials;
import hawa.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repository,
                          PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return appUser;
    }

    public Result<AppUser> create(Credentials credentials) {
        Result<AppUser> result = new Result<>();
        if (credentials.getUsername() == null || credentials.getUsername().isBlank()) {
            result.addMessage( "Username is required",ResultType.INVALID);
            return result;
        }

        if (credentials.getUsername().length() > 50) {
            result.addMessage( "Username must be less than 50 characters",ResultType.INVALID);
        }

        if (credentials.getPassword() == null || credentials.getPassword().length() < 8) {
            result.addMessage( "Password must be at least 8 characters",ResultType.INVALID);
            return result;
        }

        if (!validatePassword(credentials.getPassword())){
            result.addMessage(
                    "Password must contain a digit, a letter, and a non-digit/non-letter",ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        AppUser appUser = new AppUser(0,  credentials.getUsername(), encoder.encode(credentials.getPassword()),
                false, List.of("User"));

        result.setPayload(repository.add(appUser));
        return result;
    }

    private boolean validatePassword(String password) {
        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        return digits != 0 && letters != 0 && others != 0;
    }
}
