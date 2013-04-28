// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.service;

import edu.rit.asksg.domain.Identity;
import edu.rit.asksg.repository.IdentityRepository;
import edu.rit.asksg.service.IdentityServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect IdentityServiceImpl_Roo_Service {
    
    declare @type: IdentityServiceImpl: @Service;
    
    declare @type: IdentityServiceImpl: @Transactional;
    
    @Autowired
    IdentityRepository IdentityServiceImpl.identityRepository;
    
    public long IdentityServiceImpl.countAllIdentitys() {
        return identityRepository.count();
    }
    
    public void IdentityServiceImpl.deleteIdentity(Identity identity) {
        identityRepository.delete(identity);
    }
    
    public Identity IdentityServiceImpl.findIdentity(Long id) {
        return identityRepository.findOne(id);
    }
    
    public List<Identity> IdentityServiceImpl.findAllIdentitys() {
        return identityRepository.findAll();
    }
    
    public List<Identity> IdentityServiceImpl.findIdentityEntries(int firstResult, int maxResults) {
        return identityRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void IdentityServiceImpl.saveIdentity(Identity identity) {
        identityRepository.save(identity);
    }
    
    public Identity IdentityServiceImpl.updateIdentity(Identity identity) {
        return identityRepository.save(identity);
    }
    
}