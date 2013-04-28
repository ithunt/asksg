package edu.rit.asksg.specification;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import org.slf4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import edu.rit.asksg.domain.Twitter;
import edu.rit.asksg.domain.Email;
import edu.rit.asksg.domain.Reddit;
import edu.rit.asksg.domain.Twilio;
import edu.rit.asksg.domain.Facebook;


public class ServiceSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

	@Log
	Logger log;

	private final String service;

	public ServiceSpecification(Service service) {
		this.service = service.getName();
	}

	public ServiceSpecification(String service) {
		this.service = service;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		final Join<T, Service> join = root.join("service");
		Predicate p = cb.equal(root.get("id"), root.get("id"));
		try {
			p = cb.equal(join.type(), cb.literal(Class.forName("edu.rit.asksg.domain." + service)));
		} catch (ClassNotFoundException e1) {
			log.error("Failed to load class: " + service + " when filtering out services.", e1);
		}
		return p;
	}
}
