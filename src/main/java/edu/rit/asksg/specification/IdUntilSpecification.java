package edu.rit.asksg.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class IdUntilSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

    private final long id;

    public IdUntilSpecification(long id) {
        this.id = id;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        final Path<Long> id = root.get("id");
        return cb.lessThanOrEqualTo(id, this.id);
    }
}
