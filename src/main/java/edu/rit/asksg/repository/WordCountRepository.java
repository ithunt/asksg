package edu.rit.asksg.repository;

import edu.rit.asksg.analytics.domain.WordCount;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = WordCount.class)
public interface WordCountRepository {
}
