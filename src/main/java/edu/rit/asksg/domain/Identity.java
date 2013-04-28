package edu.rit.asksg.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;

@RooJavaBean
@RooJpaEntity
public abstract class Identity {

	private boolean enabled = true;

	private String name;
	private String email;
	private String phoneNumber;

	public void setEmail(String email) {
		this.email = email;
		if (name == null) name = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		if (name == null) name = phoneNumber;
	}
}
