package edu.rit.asksg.specification;

import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TagSpecification<T> extends AbstractSpecification<T> implements Specification<T> {

	private final String tagName;

	public TagSpecification(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		final Join<T, Message> messageJoin = root.join("messages");
		final Join<Message, Tag> tags = messageJoin.join("tags");
		return criteriaBuilder.equal(tags.get("name"), tagName);
	}
}
