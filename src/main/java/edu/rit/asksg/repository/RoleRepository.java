package edu.rit.asksg.repository;

import edu.rit.asksg.domain.UserRole;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = UserRole.class)
public interface RoleRepository {
}
