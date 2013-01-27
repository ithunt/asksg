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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AsksgUser.class);

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
            List<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
            for(UserRole role : roles) {
                logger.debug("Adding " + role + " to user " + userName);
                authoritiesList.add(new SimpleGrantedAuthority(role.getName()));
            }
            authorities = authoritiesList;
        }
        return authorities;
    }

    public void setAuthorities(List<java.lang.String> roles) {
        List<GrantedAuthority> listOfAuthorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            listOfAuthorities.add(new SimpleGrantedAuthority(role));
        }
        authorities = listOfAuthorities;
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
