package edu.rit.asksg.domain.config;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.Person;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.social.ApiBinding;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJson(deepSerialize = true)
@RooJpaEntity
public class SpringSocialConfig extends ProviderConfig {

	@NotNull
	private String handle;


	@ManyToOne
	private Person person;

	private String url;

	@ManyToOne
	private AsksgUser createdBy;

	//todo implement JPA Repo version of userconfigrep, switch out apibinding to correct type - so transient for now
	private transient ApiBinding apiBinding;

	public ApiBinding getApiBinding() {
		return apiBinding;
	}

	public void setApiBinding(ApiBinding apiBinding) {
		this.apiBinding = apiBinding;
	}
}
