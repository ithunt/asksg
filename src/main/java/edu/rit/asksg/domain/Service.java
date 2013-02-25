package edu.rit.asksg.domain;

import java.util.List;

import edu.rit.asksg.service.ProviderService;
import org.joda.time.LocalDateTime;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Service {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ProviderConfig config;

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public List<edu.rit.asksg.domain.Conversation> getNewContent() {
        return null;
    }

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
