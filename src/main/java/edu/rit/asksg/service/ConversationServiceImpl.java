package edu.rit.asksg.service;

import com.google.common.base.Optional;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.specification.AbstractSpecification;
import edu.rit.asksg.specification.ConversationServiceSpecification;
import edu.rit.asksg.specification.ConversationSinceSpecification;
import edu.rit.asksg.specification.ConversationUntilSpecification;
import edu.rit.asksg.specification.Specification;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConversationServiceImpl implements ConversationService {

	@Autowired
	MessageService messageService;

	@Autowired
	ProviderService providerService;

	public void saveConversations(Collection<Conversation> conversations) {
		for (Conversation conversation : conversations) {
			saveConversation(conversation);
		}
	}

	public void saveConversation(Conversation conversation) {
		for (Message m : conversation.getMessages()) {
			if (m.getContent() == null) m.setContent("");
			//TODO make 2000 more visible?
			if (m.getContent().length() > 2000) m.setContent(m.getContent().substring(0, 2000));
		}
		conversationRepository.save(conversation);
	}

	public Conversation updateConversation(Conversation conversation) {
		conversation.setModified(LocalDateTime.now());
		return conversationRepository.save(conversation);
	}


    public List<Conversation> findByService(final Service service, final LocalDateTime since, final LocalDateTime until) {
        Specification<Conversation> sinceSpec = new ConversationSinceSpecification(since);
        Specification<Conversation> untilSpec = new ConversationUntilSpecification(until);
        Specification<Conversation> serviceSpec = new ConversationServiceSpecification(service);

        Specification<Conversation> spec = sinceSpec.and(untilSpec.and(serviceSpec));

        return conversationRepository.findAll(spec);
    }

    public List<Conversation> findByService(final Service service, final LocalDateTime since) {
        return findByService(service, since, LocalDateTime.now());
    }

	@Override
	public List<Conversation> findAllConversations(
			final Optional<Integer> since,
			final Optional<Integer> until,
			final Long[] excludeServices,
			final String[] includeTags,
			final Optional<Boolean> showRead,
			final int count) {

		Specification<Conversation> specification = new AbstractSpecification<Conversation>() {
			@Override
			public Predicate toPredicate(Root<Conversation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> predicates = new ArrayList<Predicate>();
				Path<Integer> id = root.get("id");
				if (since.isPresent()) {
					predicates.add(cb.gt(id, since.get()));
				} else if (until.isPresent()) {
					predicates.add(cb.lt(id, until.get()));
				}

				Join<Conversation, Service> join = root.join("service");

				for (Long service : excludeServices) {
					predicates.add(cb.notEqual(join.get("id"), service));
				}

				//todo: this doesnt work, tags are on messages
				for (String tag : includeTags) {
					predicates.add(cb.equal(root.get("tag.name"), tag));
				}

				if (showRead.isPresent())
					predicates.add(cb.equal(root.get("isRead"), showRead.get()));

				query.orderBy(cb.desc(root.get("created")));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};

		return ((Page<Conversation>) conversationRepository.findAll(specification, new PageRequest(0, count))).getContent();
	}

	@Override
	public Conversation findConversationByRecipient(String recipient) {
		List<Message> messages = messageService.findMessagesByAuthor(recipient);
		if (! messages.isEmpty()) {
			return messages.get(0).getConversation();
		} else {
			return null;
		}
	}
}
