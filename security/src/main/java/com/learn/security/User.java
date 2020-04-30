package com.learn.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private Boolean enabled;
    private boolean locked;

    private List<Role> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * 账号是否未锁定，我们数据库中有该字段，所以直接返回我们的字段即可
     * 同时删除对应的get方法，因为这个方法相当于get
     * enabled同理
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
    /**
     * 凭证(密码)是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 返回用户的所有角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将roles进行整理
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            // 角色认证的要求，角色名必须是ROLE开始的（如果数据库中查出来的数据就是ROLE开始，该字符串可以省略）
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }
        return authorities;
    }
}
