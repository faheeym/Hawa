package hawa.controllers;

import hawa.domain.Result;
import hawa.domain.ResultType;
import hawa.domain.UserProfileService;
import hawa.models.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/userprofile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfile> findByUsername(@PathVariable String username){
        UserProfile userProfile = userProfileService.findByUsername(username);
        if(userProfile==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userProfile);

    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody UserProfile userProfile) {
        Result<UserProfile> result = userProfileService.add(userProfile);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{user_profile_id}")
    public ResponseEntity<Object> update(@RequestBody UserProfile userProfile, @PathVariable int user_profile_id){
        if (userProfile.getUser_profile_id() != user_profile_id){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<UserProfile> result = userProfileService.update(userProfile);
        if (result.getType() == ResultType.INVALID){
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }else if(result.getType() == ResultType.NOT_FOUND){
            return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
