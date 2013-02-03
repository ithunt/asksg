package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Person;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Person.class)
public interface PersonRepository {
}
