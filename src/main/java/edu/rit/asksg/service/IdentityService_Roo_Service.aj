// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.service;

import edu.rit.asksg.domain.Identity;
import edu.rit.asksg.service.IdentityService;
import java.util.List;

privileged aspect IdentityService_Roo_Service {
    
    public abstract long IdentityService.countAllIdentitys();    
    public abstract void IdentityService.deleteIdentity(Identity identity);    
    public abstract Identity IdentityService.findIdentity(Long id);    
    public abstract List<Identity> IdentityService.findAllIdentitys();    
    public abstract List<Identity> IdentityService.findIdentityEntries(int firstResult, int maxResults);    
    public abstract void IdentityService.saveIdentity(Identity identity);    
    public abstract Identity IdentityService.updateIdentity(Identity identity);    
}