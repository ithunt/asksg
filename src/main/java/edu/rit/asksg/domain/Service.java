package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.config.ProviderConfig;
import flexjson.JSON;
import org.joda.time.LocalDateTime;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson(deepSerialize = true)
public class Service implements ContentProvider {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ProviderConfig config;

	public String getName() {
		return this.getClass().getSimpleName();
	}

	@JSON(include = false)
	public List<edu.rit.asksg.domain.Conversation> getNewContent() {
		return null;
	}

	@JSON(include = false)
	public List<edu.rit.asksg.domain.Conversation> getContentSince(LocalDateTime datetime) {
		return null;
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
}
