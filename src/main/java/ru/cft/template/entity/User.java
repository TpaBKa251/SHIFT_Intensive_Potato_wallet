package ru.cft.template.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @Size(min = 1, max = 50, message = "Name must be at least 1 character and no more than 50")
    @NotBlank(message = "User first name cannot be empty")
    private String firstName;

    @Size(min = 1, max = 50, message = "Last name must be at least 1 character and no more than 50")
    @NotBlank(message = "User last name cannot be empty")
    private String lastName;

    @Size(min = 1, max = 50, message = "Middle name must be at least 1 character and no more than 50")
    private String middleName;

    @Column(unique = true, nullable = false, length = 30)
    @Size(min = 5, max = 50, message = "Email must be at least 5 character and no more than 50")
    @NotBlank(message = "User email cannot be empty")
    @Email(message = "Invalid email")
    private String email;

    @Column(nullable = false)
    @Min(value = 18, message = "Age must be at least 18")
    @NotBlank(message = "User birthdate cannot be empty")
    private Integer age;

    @Column(nullable = false)
    private Date registrationDate = new Date();

    @Column(nullable = false)
    private Date lastUpdateDate = new Date();

    @Column(unique = true)
    @Size(min = 11, max = 11, message = "Phone number must be 11 characters")
    @NotBlank(message = "User phone number cannot be empty")
    private Long phoneNumber;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "User password cannot be empty")
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
