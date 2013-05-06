package edu.rit.asksg.specification;

import org.joda.time.LocalDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CreatedUntilSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

    private final LocalDateTime until;

    public CreatedUntilSpecification(LocalDateTime until) {
        this.until = until;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        final Path<LocalDateTime> date = root.get("created");
        return cb.lessThan(date, until);
    }
}
