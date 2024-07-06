package ru.cft.template.core.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

//@Data
@Entity
@Table(name = "user")
@Builder
@Setter
@Getter
@Jacksonized
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password1;
    }

    @Override
    public String getUsername() {
        return email1;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String email;
    private final String phoneNumber;
    private final LocalDate birthDate;
    private final String password;

    @CreationTimestamp
    private final LocalDateTime createdAt;

    @UpdateTimestamp
    private final LocalDateTime updatedAt;


    @Column(name = "firstName", nullable = false, length = 50, insertable=false, updatable=false)
    private String firstname;

    @Column(name = "lastName", nullable = false, length = 50, insertable=false, updatable=false)
    private String lastname;

    @ColumnDefault("NULL::character varying")
    @Column(name = "middleName", length = 50, insertable=false, updatable=false)
    private String middlename;

    @Column(name = "email", nullable = false, length = 200, insertable=false, updatable=false, unique=true)
    private String email1;

    @Column(name = "phoneNumber", nullable = false, length = 11, insertable=false, updatable=false)
    private String phonenumber;

    @Column(name = "birthDate", nullable = false, insertable=false, updatable=false)
    private LocalDate birthdate;

    @Column(name = "createdAt", nullable = false, insertable=false, updatable=false)
    private LocalTime createdat;

    @Column(name = "updatedAt", nullable = false, insertable=false, updatable=false)
    private LocalTime updatedat;

    @Column(name = "password", length = 10000, nullable = false, insertable=false, updatable=false)
    private String password1;


}
