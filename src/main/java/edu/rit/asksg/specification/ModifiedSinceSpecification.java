package edu.rit.asksg.specification;

import org.joda.time.LocalDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ModifiedSinceSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

	private final LocalDateTime since;

	public ModifiedSinceSpecification(LocalDateTime since) {
		this.since = since;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		final Path<LocalDateTime> date = root.get("modified");
		return cb.greaterThan(date, since);
	}
}
