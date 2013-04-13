package edu.rit.asksg.service;

import edu.rit.asksg.domain.Message;
import org.springframework.roo.addon.layers.service.RooService;

import java.util.List;

@RooService(domainTypes = { edu.rit.asksg.domain.Message.class })
public interface MessageService {
	public List<Message> findMessagesByAuthor(String author);
}
