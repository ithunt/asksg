package edu.rit.asksg.domain;

import java.util.List;
import org.joda.time.LocalDateTime;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Service {

    private String identifier;

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
