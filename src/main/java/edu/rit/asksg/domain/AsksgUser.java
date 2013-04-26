package edu.rit.asksg.domain;

import com.google.common.base.Optional;
import flexjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class AsksgUser extends Identity implements UserDetails {

	private static final Logger logger = LoggerFactory.getLogger(AsksgUser.class);

	@NotNull
	private String userName;

	private String password;

	private transient Optional<List<GrantedAuthority>> authorities = Optional.absent();

	@ManyToOne(fetch = FetchType.EAGER)
	private UserRole role = new UserRole();

	@JSON(include = false)
	@Override
	public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
		if (!authorities.isPresent()) {
			List<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
			authoritiesList.add(new SimpleGrantedAuthority(role.getName()));

			authorities = Optional.of(authoritiesList);
		}
		return authorities.get();
	}

	@JSON(include = false)
	public void setAuthorities(List<java.lang.String> roles) {
		List<GrantedAuthority> listOfAuthorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			listOfAuthorities.add(new SimpleGrantedAuthority(role));
		}
		authorities = Optional.of(listOfAuthorities);
	}

	// Overrides for Roo fields because SpringUserDetails implements them
	@JSON(include = false)
	@Override
	public String getPassword() {
		return password;
	}

	@JSON(include = false)
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
}
