package edu.rit.asksg.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class SocialSubscription {

    @NotNull
    private String handle;

    @ManyToOne
    private Service service;

    @ManyToOne
    private AsksgUser createdBy;

    private String name;
}
