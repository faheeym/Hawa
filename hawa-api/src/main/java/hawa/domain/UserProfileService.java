package hawa.domain;

import hawa.models.UserProfile;
import hawa.repository.AppUserRepository;
import hawa.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final AppUserRepository appUserRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, AppUserRepository appUserRepository) {
        this.userProfileRepository = userProfileRepository;
        this.appUserRepository = appUserRepository;
    }

    public UserProfile findByUsername(String username){
        return userProfileRepository.findByUsername(username);
    }

    public Result<UserProfile> update(UserProfile userProfile){
        Result<UserProfile> result = validate(userProfile);

        if(result.isSuccess()){
            if(userProfileRepository.update(userProfile)){
                result.setPayload(userProfile);
            }else{
                result.addMessage("Could Not Find That Profile",ResultType.NOT_FOUND);
            }
        }
        return result;
    }
    public Result<UserProfile> add(UserProfile userProfile){
        Result<UserProfile> result = validate(userProfile);

        if(result.isSuccess()){
            result.setPayload(userProfileRepository.add(userProfile));
        }
        return result;
    }
    private Result<UserProfile> validate(UserProfile userProfile){
        Result<UserProfile> result = new Result<>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserProfile>> violations = validator.validate(userProfile);

        if (!violations.isEmpty()){
            for (ConstraintViolation<UserProfile> violation: violations) {
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
        }
        return result;
    }
}
