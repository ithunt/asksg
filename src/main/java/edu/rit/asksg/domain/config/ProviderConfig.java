package edu.rit.asksg.domain.config;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.SocialSubscription;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import flexjson.JSON;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

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

	private LocalDateTime lastUpdate = LocalDateTime.now();

	private int maxCalls = 0;

	private int currentCalls = 0;

	private Minutes updateFrequency = Minutes.minutes(30);

	private LocalDateTime counterRefresh = LocalDateTime.now();

	@ManyToOne
	private AsksgUser createdBy;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<SocialSubscription> subscriptions = new HashSet<SocialSubscription>();

	@JSON(include = false)
	public void incrementCalls() {
		currentCalls++;
	}
}
