package edu.rit.asksg.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created with IntelliJ IDEA.
 * User: Fiskars
 * Date: 5/3/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExternalIdSpecification<T> extends AbstractSpecification<T> {

	private String externalId;

	public ExternalIdSpecification(String externalId) {
		this.externalId = externalId;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		final Path<String> externalId = root.get("externalId");
		return criteriaBuilder.equal(externalId, this.externalId);
	}
}
