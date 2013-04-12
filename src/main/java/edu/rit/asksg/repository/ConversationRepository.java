package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Service;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import java.util.List;

@RooJpaRepository(domainType = Conversation.class)
public interface ConversationRepository {
}
