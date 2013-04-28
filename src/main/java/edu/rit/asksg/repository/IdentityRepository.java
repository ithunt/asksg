package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Identity;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Identity.class)
public interface IdentityRepository {
}
