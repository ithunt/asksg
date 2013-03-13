package edu.rit.asksg.domain.config;

import flexjson.JSON;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.social.ApiBinding;

import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaEntity
public class SpringSocialConfig extends ProviderConfig {

	@NotNull
	private String handle;

	private String url;

	//todo implement JPA Repo version of userconfigrep, switch out apibinding to correct type - so transient for now
	private transient ApiBinding apiBinding;

	@JSON(include = false)
	public ApiBinding getApiBinding() {
		return apiBinding;
	}

	public void setApiBinding(ApiBinding apiBinding) {
		this.apiBinding = apiBinding;
	}
}
