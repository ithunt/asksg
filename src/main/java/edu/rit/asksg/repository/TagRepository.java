package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Tag;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Tag.class)
public interface TagRepository {
	Tag findByName(String name);
}
