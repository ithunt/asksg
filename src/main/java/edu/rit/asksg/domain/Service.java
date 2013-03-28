package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.config.EmailConfig;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.domain.config.RedditConfig;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import edu.rit.asksg.domain.config.TwilioConfig;
import flexjson.JSON;
import flexjson.JSONDeserializer;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson(deepSerialize = true)
public class Service implements ContentProvider {

	private static final transient Logger logger = LoggerFactory.getLogger(Service.class);

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ProviderConfig config;

	private boolean enabled = true;

	public String getName() {
		return this.getClass().getSimpleName();
	}

	@JSON(include = false)
	public List<edu.rit.asksg.domain.Conversation> getNewContent() {
		return new ArrayList<Conversation>();
	}

	@JSON(include = false)
	public List<edu.rit.asksg.domain.Conversation> getContentSince(LocalDateTime datetime) {
		return new ArrayList<Conversation>();
	}

	public boolean postContent(Message message) {
		return true;
	}

	public boolean authenticate() {
		return false;
	}

	public boolean isAuthenticated() {
		return false;
	}

	public static Service fromJsonToService(String json) {
		logger.trace(json);
		Service s;
		if (json.contains("\"name\":\"Twilio\"")) {
			s = new JSONDeserializer<Service>().use(null, Twilio.class).use("config", TwilioConfig.class).deserialize(json);
		} else if (json.contains("\"name\":\"Email\"")) {
			s = new JSONDeserializer<Service>().use(null, Email.class).use("config", EmailConfig.class).deserialize(json);
		} else if (json.contains("\"name\":\"Facebook\"")) {
			s = new JSONDeserializer<Service>().use(null, Facebook.class).use("config", SpringSocialConfig.class).deserialize(json);
		} else if (json.contains("\"name\":\"Twitter\"")) {
			s = new JSONDeserializer<Service>().use(null, Twitter.class).use("config", SpringSocialConfig.class).deserialize(json);
		} else if (json.contains("\"name\":\"Reddit\"")) {
			s = new JSONDeserializer<Service>().use(null, Reddit.class).use("config", RedditConfig.class).deserialize(json);
		} else {
			s = new JSONDeserializer<Service>().use(null, Service.class).deserialize(json);
		}
		logger.trace(s.toString());
		return s;
	}
}
