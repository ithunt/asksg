package edu.rit.asksg.specification;

import edu.rit.asksg.domain.Service;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ServiceSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

    private final Service service;

    public ServiceSpecification(Service service) {
        this.service = service;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Join<T, Service> join = root.join("service");
        return cb.equal(join.get("id"), service.getId());
    }
}
