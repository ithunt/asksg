package edu.rit.asksg.service;


import edu.rit.asksg.domain.Service;

import javax.annotation.Resource;
import java.util.Map;

public class ProviderServiceImpl implements ProviderService {

    @Resource
    Map<String, Service> providerMap;

    private boolean strapped = false;

    @Override
    public void bootstrap() {
        if(strapped) return;

        for(Map.Entry<String, Service> entry : providerMap.entrySet()) {
            serviceRepository.save(entry.getValue());
        }
    }
}
