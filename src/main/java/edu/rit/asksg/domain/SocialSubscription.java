package edu.rit.asksg.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class SocialSubscription {

    public SocialSubscription() {
    }

    public SocialSubscription(String name, String handle) {
        this.name = name;
        this.handle = handle;
    }

    @NotNull
    private String handle;

    private String name;
}
