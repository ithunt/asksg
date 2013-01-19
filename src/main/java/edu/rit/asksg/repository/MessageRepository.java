package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Message;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Message.class)
public interface MessageRepository {
}
