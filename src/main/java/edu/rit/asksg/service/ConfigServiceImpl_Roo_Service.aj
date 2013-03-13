// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.service;

import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.repository.ConfigRepository;
<<<<<<< HEAD
=======
import edu.rit.asksg.service.ConfigServiceImpl;
import java.util.List;
>>>>>>> origin/master
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

privileged aspect ConfigServiceImpl_Roo_Service {
    
    declare @type: ConfigServiceImpl: @Service;
    
    declare @type: ConfigServiceImpl: @Transactional;
    
    @Autowired
    ConfigRepository ConfigServiceImpl.configRepository;
    
    public long ConfigServiceImpl.countAllProviderConfigs() {
        return configRepository.count();
    }
    
    public void ConfigServiceImpl.deleteProviderConfig(ProviderConfig providerConfig) {
        configRepository.delete(providerConfig);
    }
    
    public ProviderConfig ConfigServiceImpl.findProviderConfig(Long id) {
        return configRepository.findOne(id);
    }
    
    public List<ProviderConfig> ConfigServiceImpl.findAllProviderConfigs() {
        return configRepository.findAll();
    }
    
    public List<ProviderConfig> ConfigServiceImpl.findProviderConfigEntries(int firstResult, int maxResults) {
        return configRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void ConfigServiceImpl.saveProviderConfig(ProviderConfig providerConfig) {
        configRepository.save(providerConfig);
    }
    
    public ProviderConfig ConfigServiceImpl.updateProviderConfig(ProviderConfig providerConfig) {
        return configRepository.save(providerConfig);
    }
    
}
