package edu.rit.asksg.specification;

import edu.rit.asksg.domain.Identity;
import edu.rit.asksg.domain.Message;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

public class AuthorSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

	private String author;

	public AuthorSpecification(String author) {
		this.author = author;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		final Join<T, Message> messageJoin = root.join("messages");
		final Join<Message, Identity> identity = messageJoin.join("identity");
		return criteriaBuilder.equal(identity.get("name"), author);
	}
}
