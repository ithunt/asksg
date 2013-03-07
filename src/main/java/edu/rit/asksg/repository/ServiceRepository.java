package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Service.class)
public interface ServiceRepository {

	@Query("SELECT s FROM Service AS s where s.class=?1 and s.config.identifier = ?2")
	public <T extends Service> T findServiceByTypeAndIdentifierEquals(String type, String identifier);
}

