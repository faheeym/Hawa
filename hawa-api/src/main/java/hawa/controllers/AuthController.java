package hawa.controllers;

import hawa.domain.Result;
import hawa.models.AppUser;
import hawa.models.Credentials;
import hawa.security.AppUserService;
import hawa.security.JwtConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtConverter jwtConverter;
    private final AppUserService service;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtConverter jwtConverter,
                          AppUserService service) {
        this.authenticationManager = authenticationManager;
        this.jwtConverter = jwtConverter;
        this.service = service;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody Credentials credentials) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);

        if (authentication.isAuthenticated()) {
            String jwtToken = jwtConverter.getTokenFromAppUser((User) authentication.getPrincipal());

            HashMap<String, String> map = new HashMap<>();
            map.put("jwt_token", jwtToken);

            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Credentials credentials) {
        Result<AppUser> result = service.create(credentials);
        if (result.isSuccess()) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("id", result.getPayload().getId());
            return new ResponseEntity<>(map, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refresh(UsernamePasswordAuthenticationToken principal) {
        User user = new User(principal.getName(), principal.getName(), principal.getAuthorities());
        String jwtToken = jwtConverter.getTokenFromAppUser(user);
        HashMap<String, String> map = new HashMap<>();
        map.put("jwt_token", jwtToken);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
