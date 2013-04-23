package edu.rit.asksg.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotSpecification<T> extends AbstractSpecification<T> {
    private final Specification<T> a;

    public NotSpecification(Specification<T> a) {
        this.a = a;
    }

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.not(a.toPredicate(root, query, cb));
    }

}
