package edu.rit.asksg.service;

import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.SocialSubscription;
import edu.rit.asksg.specification.EqualSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProviderServiceImpl implements ProviderService {

    private static final transient Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);

	@Autowired
	private IdentityService identityService;

	@Autowired
	private ConversationService conversationService;


    public <T extends Service> T findServiceByTypeAndIdentifierEquals(Class<T> type, String identifier) {
        T service = serviceRepository.findServiceByTypeAndIdentifierEquals(type.getSimpleName(), identifier);
		if (service != null) {
			service.setIdentityService(identityService);
			service.setConversationService(conversationService);
		}
		return service;
    }

	public edu.rit.asksg.domain.Service findService(Long id) {
		Service service = serviceRepository.findOne(id);
		if (service != null) {
			service.setIdentityService(identityService);
			service.setConversationService(conversationService);
		}
		return service;
	}

	public List<Service> findAllServices() {
		List<Service> services = serviceRepository.findAll();
		for (Service service : services) {
			service.setIdentityService(identityService);
			service.setConversationService(conversationService);
		}
		return services;
	}

	public List<edu.rit.asksg.domain.Service> findServiceEntries(int firstResult, int maxResults) {
		List<Service> services = serviceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
		for (Service service : services) {
			service.setIdentityService(identityService);
			service.setConversationService(conversationService);
		}
		return services;
	}


    public void addSubscriptionToService(Long id, String name, String handle) {
        try {
            Service s = serviceRepository.findOne(new EqualSpecification<Service>("id", id));
            s.getConfig().getSubscriptions().add(new SocialSubscription(name, handle));
            serviceRepository.save(s);

        } catch (Exception e) {
            logger.error("Problem persisting social subscription to serviceid:" + id + " handle: " + handle, e);
        }
    }


}
