package edu.rit.asksg.specification;

import edu.rit.asksg.domain.Message;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

public class AuthorSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

	private String author;

	public AuthorSpecification(String author) {
		this.author = author;
	}

	@Override
	public Predicate toPredicate(Root<T> messageRoot, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.equal(messageRoot.get("author"), author);
	}
}
