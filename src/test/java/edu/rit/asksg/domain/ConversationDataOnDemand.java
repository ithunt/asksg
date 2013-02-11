package edu.rit.asksg.domain;

import org.joda.time.LocalDateTime;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Conversation.class)
public class ConversationDataOnDemand {

	public void setCreated(Conversation obj, int index) {
		obj.setCreated(LocalDateTime.now());
	}

	public void setModified(Conversation obj, int index) {
		obj.setModified(LocalDateTime.now());
	}
}
