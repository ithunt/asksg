package edu.rit.asksg.service;

import com.google.common.base.Optional;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.specification.EqualSpecification;
import edu.rit.asksg.specification.IdSinceSpecification;
import edu.rit.asksg.specification.IdUntilSpecification;
import edu.rit.asksg.specification.ServiceSpecification;
import edu.rit.asksg.specification.CreatedSinceSpecification;
import edu.rit.asksg.specification.CreatedUntilSpecification;
import edu.rit.asksg.specification.Specification;
import edu.rit.asksg.specification.TrueSpecification;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
            final Long[] excludeServices,
            final String[] includeTags,
            final Optional<Boolean> showRead,
            final int count) {

        //always true
        Specification<Conversation> spec = new TrueSpecification<Conversation>();

        if (since.isPresent()) spec = spec.and(new IdSinceSpecification<Conversation>(since.get()));
        if (until.isPresent()) spec = spec.and(new IdUntilSpecification<Conversation>(until.get()));

        for (Long serviceid : excludeServices) {
            spec = spec.and((new ServiceSpecification<Conversation>(providerService.findService(serviceid))).not());
        }

        //todo: tags

        if (showRead.isPresent()) spec = spec.and(new EqualSpecification<Conversation>("isRead", showRead.get()));

        return ((Page<Conversation>) conversationRepository.findAll(
                spec,
                new PageRequest(0, count,
                        new Sort(Sort.Direction.DESC, "created")))

        ).getContent();
    }

    @Override
    public Conversation findConversationByRecipient(String recipient) {
        List<Message> messages = messageService.findMessagesByAuthor(recipient);
        if (!messages.isEmpty()) {
            return messages.get(0).getConversation();
        } else {
            return null;
        }
    }
}
