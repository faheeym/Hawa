package hawa.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UserProfile {
private int user_profile_id;
private int app_user_id;

@NotNull(message = "Email is required")
private String email;
@NotNull(message = "First Name is required")
private String first_name;
@NotNull(message = "Last Name is required")
private String last_name;
@NotNull(message = "Birthday is required for resetting your password in future")
private LocalDate birthday;
private String relation_status;
private String profile_picture;
private String username;
}
