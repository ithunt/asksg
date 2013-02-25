package edu.rit.asksg.service;

import edu.rit.asksg.domain.Service;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { edu.rit.asksg.domain.Service.class })
public interface ProviderService {
	public <T extends Service> T findServiceByTypeAndIdentifierEquals(Class<T> type, String identifier);
}
