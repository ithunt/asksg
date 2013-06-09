package edu.rit.asksg.service;

import com.google.common.base.Optional;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.repository.MessageRepository;
import edu.rit.asksg.specification.AuthorSpecification;
import edu.rit.asksg.specification.EqualSpecification;
import edu.rit.asksg.specification.ExternalIdSpecification;
import edu.rit.asksg.specification.IdSinceSpecification;
import edu.rit.asksg.specification.IdUntilSpecification;
import edu.rit.asksg.specification.ModifiedSinceSpecification;
import edu.rit.asksg.specification.ServiceSpecification;
import edu.rit.asksg.specification.CreatedSinceSpecification;
import edu.rit.asksg.specification.CreatedUntilSpecification;
import edu.rit.asksg.specification.Specification;
import edu.rit.asksg.specification.TagSpecification;
import edu.rit.asksg.specification.TrueSpecification;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ConversationServiceImpl implements ConversationService {

	@Autowired
	MessageService messageService;

	@Autowired
	ProviderService providerService;

    @Autowired
    MessageRepository messageRepository;

	@Log
	Logger log;

	public void saveConversations(Collection<Conversation> conversations) {
		for (Conversation conversation : conversations) {
			saveConversation(conversation);
		}
    }

    public void saveConversation(Conversation conversation) {
        List<Message> seen = new ArrayList<Message>(); //prevent dupes on this object
        List<Message> keep = new ArrayList<Message>(); //white list non dupes

        for (Message m : conversation.getMessages()) {
            //Messages should not be null or empty
            if(m.getContent() == null) {
                log.warn("Null content on " + conversation.getService().getName() + " created " + conversation.getCreated().toDate().toString());
            }

            //update is also a save
            else if (m.getId() != null) {
                keep.add(m);
            }

            //Has this message already appeared in this conversation?
            else if (!seen.contains(m)) {

                if (m.getContent().length() > 2000) m.setContent(m.getContent().substring(0, 2000));

                //Check to see if this message already exists in the system, based on content and creation time
                Specification<Message> spec = new EqualSpecification<Message>("created", m.getCreated());
                //spec = spec.and(new EqualSpecification<Message>("identity", m.getIdentity()));
                List<Message> ms = messageRepository.findAll(spec);

                if (ms.size() == 0) {
                    keep.add(m); //Not a duplicate, we're good
                } else {
                    log.warn("Duplicate message found on " + conversation.getService().getName() + ": " + m.getContent());
                }
            }
            seen.add(m);
        }

        //Messages check out, persist them
        if(keep.size() > 0) {
            conversation.setMessages(keep);
            conversationRepository.save(conversation);
        } else {
            log.warn("Conversation empty or all duplicates, skipping persistence - " + conversation.getService().getName());
        }
    }

    public Conversation updateConversation(Conversation conversation) {
        conversation.setModified(LocalDateTime.now());
        return conversationRepository.save(conversation);
	}

	public List<Conversation> findByService(final Service service, final LocalDateTime since, final LocalDateTime until) {
		Specification<Conversation> spec = new CreatedSinceSpecification<Conversation>(since);
		spec = spec.and(new CreatedUntilSpecification<Conversation>(until));
		spec = spec.and(new ServiceSpecification<Conversation>(service));

		return conversationRepository.findAll(spec);
	}

	public List<Conversation> findByService(final Service service, final LocalDateTime since) {
		return findByService(service, since, LocalDateTime.now());
	}

	@Override
	public List<Conversation> findAllConversations(
			final Optional<Integer> since,
			final Optional<Integer> until,
			final String[] includeServices,
			final String[] includeTags,
			final Optional<Boolean> showRead,
			final int count) {

		//always true
		Specification<Conversation> spec = new TrueSpecification<Conversation>();
		if (since.isPresent()) spec = spec.and(new IdSinceSpecification<Conversation>(since.get()));
		if (until.isPresent()) spec = spec.and(new IdUntilSpecification<Conversation>(until.get()));
		if (showRead.isPresent()) spec = spec.and(new EqualSpecification<Conversation>("isRead", showRead.get()));

		Iterator<String> serviceIterator = Arrays.asList(includeServices).iterator();
		if(serviceIterator.hasNext()){
			spec = spec.and(new ServiceSpecification<Conversation>(serviceIterator.next()));
			while (serviceIterator.hasNext()){
				spec = spec.or(new ServiceSpecification<Conversation>(serviceIterator.next()));
			}
		}

		Iterator<String> tagIterator = Arrays.asList(includeTags).iterator();
		if(tagIterator.hasNext()){
			spec = spec.and(new TagSpecification<Conversation>(tagIterator.next()));
			while(tagIterator.hasNext()){
				spec = spec.or(new TagSpecification<Conversation>(tagIterator.next()));
			}
		}

		return ((Page<Conversation>) conversationRepository.findAll(
				spec,
				new PageRequest(0, count,
						new Sort(Sort.Direction.DESC, "created")))

		).getContent();
	}

	@Override
	public Conversation findConversationByRecipientSince(String recipient, LocalDateTime since) {
		ModifiedSinceSpecification<Conversation> sinceSpec = new ModifiedSinceSpecification<Conversation>(since);
		AuthorSpecification<Conversation> authorSpec = new AuthorSpecification<Conversation>(recipient);
		return conversationRepository.findOne(sinceSpec.and(authorSpec));
	}

	@Override
	public Conversation findConversationByExternalId(String externalId) {
		ExternalIdSpecification<Conversation> idSpec = new ExternalIdSpecification<Conversation>(externalId);
		return conversationRepository.findOne(idSpec);
	}
}
