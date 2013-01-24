package edu.rit.asksg.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findAsksgUsersByUserNameEquals" })
@RooJson(deepSerialize = true)
public class AsksgUser implements UserDetails {

    @NotNull
    private String name;

    @NotNull
    private String userName;

    @NotNull
    private String password;

    private transient List<GrantedAuthority> authorities;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<UserRole> roles = new HashSet<UserRole>();

    @Override
    public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            populateAuthorities();
        }
        return authorities;
    }

    public void setAuthorities(List<java.lang.String> roles) {
        Set<UserRole> userRoles = new HashSet<UserRole>();
        for(String r : roles) {
            userRoles.add(new UserRole(r));
        }
        this.roles = userRoles;
        populateAuthorities();
    }

    protected void populateAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        this.authorities = authorities;

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
}
