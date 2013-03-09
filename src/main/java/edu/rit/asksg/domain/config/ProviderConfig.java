package edu.rit.asksg.domain.config;

import edu.rit.asksg.domain.AsksgUser;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class ProviderConfig {

	private String authenticationToken;

	private String identifier;

	private String username;

	private String password;

	private String host;

	private int port;

	@ManyToOne
	private AsksgUser createdBy;
}
