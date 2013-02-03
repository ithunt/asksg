package edu.rit.asksg.repository;

import edu.rit.asksg.domain.SocialSubscription;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = SocialSubscription.class)
public interface SocialSubscriptionRespository {
}
