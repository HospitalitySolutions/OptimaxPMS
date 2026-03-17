package com.optimax.pms.security;

import com.optimax.pms.user.User;
import com.optimax.pms.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final boolean active;
    private final UserRole role;

    public UserPrincipal(Long id, String email, String password, boolean active, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.active = active;
        this.role = role;
    }

    public static UserPrincipal fromEntity(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                user.getRole()
        );
    }

    public Long getId() {
        return id;
    }

    public UserRole getRole() {
        return role;
    }

    /**
     * Lightweight reference to the underlying user entity for associations.
     * Avoids loading full user graph.
     */
    public User toUserEntityRef() {
        User user = new User();
        user.setId(this.id);
        user.setEmail(this.email);
        user.setRole(this.role);
        user.setActive(this.active);
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}

