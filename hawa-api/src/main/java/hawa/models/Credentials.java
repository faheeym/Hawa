package hawa.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Credentials {
    private String username;
    private String password;
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

}
