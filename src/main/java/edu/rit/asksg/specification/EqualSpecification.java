package edu.rit.asksg.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EqualSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

    private final String propertyName;
    private final Object equalTo;

    public EqualSpecification(String propertyName, Object equalTo) {
        this.propertyName = propertyName;
        this.equalTo = equalTo;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get(propertyName), equalTo);
    }
}
