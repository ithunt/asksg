// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.service;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.service.UserService;
import java.util.List;

privileged aspect UserService_Roo_Service {
    
    public abstract long UserService.countAllAsksgUsers();    
    public abstract void UserService.deleteAsksgUser(AsksgUser asksgUser);    
    public abstract AsksgUser UserService.findAsksgUser(Long id);    
    public abstract List<AsksgUser> UserService.findAllAsksgUsers();    
    public abstract List<AsksgUser> UserService.findAsksgUserEntries(int firstResult, int maxResults);    
    public abstract void UserService.saveAsksgUser(AsksgUser asksgUser);    
    public abstract AsksgUser UserService.updateAsksgUser(AsksgUser asksgUser);    
}
