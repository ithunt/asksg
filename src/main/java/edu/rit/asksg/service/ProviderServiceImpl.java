package edu.rit.asksg.service;

import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.SocialSubscription;
import edu.rit.asksg.specification.EqualSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProviderServiceImpl implements ProviderService {

    private static final transient Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);


	public <T extends Service> T findServiceByTypeAndIdentifierEquals(Class<T> type, String identifier) {
		return serviceRepository.findServiceByTypeAndIdentifierEquals(type.getSimpleName(), identifier);
	}


    public void addSubscriptionToService(Long id, String name, String handle) {
        try {
            Service s =  serviceRepository.findOne(new EqualSpecification<Service>("id", id));
            s.getConfig().getSubscriptions().add(new SocialSubscription(name, handle));
            serviceRepository.save(s);

        } catch (Exception e) {
            logger.error("Problem persisting social subscription to serviceid:" + id + " handle: " + handle, e);
        }
    }



}
