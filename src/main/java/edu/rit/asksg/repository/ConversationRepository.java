package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Conversation;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Conversation.class)
public interface ConversationRepository {
}
