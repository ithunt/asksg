package edu.rit.asksg.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AndSpecification<T> extends AbstractSpecification<T> {
    private final Specification<T> a;
    private final Specification<T> b;

    public AndSpecification(Specification<T> a, Specification<T> b) {
        this.a = a;
        this.b = b;
    }

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.and(a.toPredicate(root, query, cb), b.toPredicate(root, query, cb));
    }
}