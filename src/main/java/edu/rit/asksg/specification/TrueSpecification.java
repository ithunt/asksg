package edu.rit.asksg.specification;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TrueSpecification extends AbstractSpecification {
    @Override
    public Predicate toPredicate(Root root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        //todo: Test this
        return cb.equal(root.get("id"), root.get("id"));
    }
}
