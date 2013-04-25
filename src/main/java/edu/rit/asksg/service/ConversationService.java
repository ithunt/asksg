package edu.rit.asksg.service;

import com.google.common.base.Optional;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Service;
import org.joda.time.LocalDateTime;
import org.springframework.roo.addon.layers.service.RooService;

import java.util.Collection;
import java.util.List;

@RooService(domainTypes = {edu.rit.asksg.domain.Conversation.class})
public interface ConversationService {
	public void saveConversations(Collection<Conversation> conversations);

	List<Conversation> findAllConversations(
			Optional<Integer> since,
			Optional<Integer> until,
			String[] excludeServices,
			String[] includeTags,
			Optional<Boolean> showRead,
			int count);

	List<Conversation> findByService(Service service, LocalDateTime since);

	List<Conversation> findByService(Service service, LocalDateTime since, LocalDateTime until);

	Conversation findConversationByRecipient(
			String recipient
	);

}
