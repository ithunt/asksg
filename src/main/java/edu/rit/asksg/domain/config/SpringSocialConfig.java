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

	private String url;

	private String consumerKey;

	private String consumerSecret;

	private String accessToken;

	private String accessTokenSecret;


}
