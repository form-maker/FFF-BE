package com.formmaker.fff.common.response.security;


import com.formmaker.fff.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final String loginId;
    private final String password;
    private final Long userId;

    private final String username;

    public UserDetailsImpl(User user) {
        this.user = user;
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.userId = user.getId();
        this.username = user.getUsername();
    }

    public Long getUserId(){
        return this.userId;
    }
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
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
