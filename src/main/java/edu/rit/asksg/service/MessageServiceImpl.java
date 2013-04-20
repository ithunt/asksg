package edu.rit.asksg.service;

import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    ConversationService conversationService;

    /**
     * Post a message with its outgoing service (reply), and then save attached conversation object
     *
     * @param message Populated message with a conversation object attached
     */
    public void saveMessage(Message message) {
        try {
            message.setPosted(postMessage(message));
            conversationService.updateConversation(message.getConversation());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            //TODO: indicate to user that the service they're using is unavailable and message might not have been sent
        }
    }

    protected boolean postMessage(Message message) {
        final Service service = message.getConversation().getService();
        return service.postContent(message);
    }

	public List<Message> findMessagesByAuthor(final String author) {
		Specification<Message> specification = new Specification<Message>() {
			@Override
			public Predicate toPredicate(Root<Message> messageRoot, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(messageRoot.get("author"), author);
			}
		};
		return messageRepository.findAll(specification);
	}
}
