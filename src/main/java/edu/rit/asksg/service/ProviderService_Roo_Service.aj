// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.service;

import edu.rit.asksg.domain.Service;
import edu.rit.asksg.service.ProviderService;
import java.util.List;

privileged aspect ProviderService_Roo_Service {
    
    public abstract long ProviderService.countAllServices();    
    public abstract void ProviderService.deleteService(Service service);    
    public abstract Service ProviderService.findService(Long id);    
    public abstract List<Service> ProviderService.findAllServices();    
    public abstract List<Service> ProviderService.findServiceEntries(int firstResult, int maxResults);    
    public abstract void ProviderService.saveService(Service service);    
    public abstract Service ProviderService.updateService(Service service);    
}