package edu.rit.asksg.domain;

import org.joda.time.LocalDateTime;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Message.class)
public class MessageDataOnDemand {

	public void setCreated(Message obj, int index) {
		obj.setCreated(LocalDateTime.now());
	}

	public void setModified(Message obj, int index) {
		obj.setModified(LocalDateTime.now());
	}
}
