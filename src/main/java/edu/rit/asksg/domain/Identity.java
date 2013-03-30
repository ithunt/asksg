package edu.rit.asksg.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;

@RooJavaBean
@RooJpaEntity
public abstract class Identity {

	private boolean enabled = true;

	public abstract String getName();

	public abstract String getEmail();

	public abstract String getPhoneNumber();

}
