package com.powersense.auth.domain.model.aggregates;

import com.powersense.auth.domain.model.commands.RegisterUserCommand;
import com.powersense.auth.domain.model.commands.UpdateUserProfileCommand;
import com.powersense.auth.domain.model.valueobjects.Email;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected User() {
    }

    public User(RegisterUserCommand command, String encodedPassword) {
        if (command == null) {
            throw new IllegalArgumentException("RegisterUserCommand cannot be null");
        }
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("Encoded password cannot be null or empty");
        }

        this.email = new Email(command.email());
        this.passwordHash = encodedPassword;
        this.name = command.name();
        this.avatarUrl = "";
        this.isActive = true;
    }

    public void updateProfile(UpdateUserProfileCommand command) {
        if (command.name() != null && !command.name().isBlank()) {
            this.name = command.name();
        }
        if (command.avatarUrl() != null) {
            this.avatarUrl = command.avatarUrl();
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public String getEmailValue() {
        return email.getValue();
    }

    public String getInitials() {
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    public boolean hasAvatar() {
        return avatarUrl != null && !avatarUrl.isBlank();
    }
}