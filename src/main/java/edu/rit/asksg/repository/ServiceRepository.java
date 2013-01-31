package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Service;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Service.class)
public interface ServiceRepository {
}
