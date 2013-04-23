package edu.rit.asksg.service;

import edu.rit.asksg.domain.Service;

public class ProviderServiceImpl implements ProviderService {

	public <T extends Service> T findServiceByTypeAndIdentifierEquals(Class<T> type, String identifier) {
		return serviceRepository.findServiceByTypeAndIdentifierEquals(type.getSimpleName(), identifier);
	}


    public void addSubscriptionToService(Long id, String name, String handle) {

        //need specification changes



    }



}
