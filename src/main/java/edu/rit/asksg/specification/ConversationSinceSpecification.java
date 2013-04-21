package edu.rit.asksg.specification;


import edu.rit.asksg.domain.Conversation;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ConversationSinceSpecification extends AbstractSpecification<Conversation> implements Specification<Conversation> {


    private final LocalDateTime since;

    public ConversationSinceSpecification(LocalDateTime since) {
        this.since = since;
    }

    @Override
    public Predicate toPredicate(Root<Conversation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        final Path<LocalDateTime> date = root.get("created");
        return cb.greaterThan(date, since);
    }
}
