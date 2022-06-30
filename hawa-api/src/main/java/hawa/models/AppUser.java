package hawa.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AppUser extends User {

    private static final String AUTHORITY_PREFIX = "ROLE_";

    private int id;
    @NotNull(message = "Gender is required")
    private String gender;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "First Name is required")
    private String first_name;
    @NotNull(message = "Last Name is required")
    private String last_name;
    @NotNull(message = "Birthday is required for resetting your password in future")
    private String birthday;
    private String relation_status;
    private String profile_picture;

    public AppUser(int id, String gender, String email, String first_name, String last_name, String birthday, String relation_status, String profile_picture, String username, String password,
                   boolean disabled, List<String> roles) {
        super(username, password, !disabled,
                true, true, true,
                convertRolesToAuthorities(roles));
        this.id = id;
        this.gender=gender;
        this.email=email;
        this.first_name=first_name;
        this.last_name=last_name;
        this.birthday=birthday;
        this.relation_status=relation_status;
        this.profile_picture=profile_picture;

    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday (String birthday) {
        this.birthday = birthday;
    }

    public String getRelation_status() {
        return relation_status;
    }

    public void setRelation_status(String relation_status) {
        this.relation_status = relation_status;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }



    private List<String> roles = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (String role : roles) {
            Assert.isTrue(!role.startsWith(AUTHORITY_PREFIX),
                    () -> String.format("%s cannot start with %s (it is automatically added)",
                                            role, AUTHORITY_PREFIX));
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role));
        }
        return authorities;
    }

    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> a.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }
}