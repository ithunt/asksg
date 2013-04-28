package edu.rit.asksg.repository;

import edu.rit.asksg.analytics.domain.Topic;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Topic.class)
public interface TopicRepository {
}
