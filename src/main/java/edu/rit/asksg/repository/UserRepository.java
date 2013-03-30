package edu.rit.asksg.repository;

import edu.rit.asksg.domain.AsksgUser;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = AsksgUser.class)
public interface UserRepository {
	AsksgUser findByUserName(String username);

}
