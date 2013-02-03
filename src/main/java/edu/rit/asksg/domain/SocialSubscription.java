package edu.rit.asksg.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson(deepSerialize = true)
@RooJpaActiveRecord(finders = { "findSocialSubscriptionsByPerson", "findSocialSubscriptionsByProvider", "findSocialSubscriptionsByHandleEquals" })
public class SocialSubscription {

    @NotNull
    private String handle;

    @ManyToOne
    @NotNull
    private Service provider;

    @ManyToOne
    private Person person;
}
