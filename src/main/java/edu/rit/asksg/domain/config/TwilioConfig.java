package edu.rit.asksg.domain.config;

import edu.rit.asksg.domain.Twilio;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class TwilioConfig extends ProviderConfig {
}
