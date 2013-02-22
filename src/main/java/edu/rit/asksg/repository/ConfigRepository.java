package edu.rit.asksg.repository;

import edu.rit.asksg.domain.ProviderConfig;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = ProviderConfig.class)
public interface ConfigRepository {
}
