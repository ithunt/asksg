// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain.config;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.Person;
import edu.rit.asksg.domain.config.SpringSocialConfig;

privileged aspect SpringSocialConfig_Roo_JavaBean {
    
    public String SpringSocialConfig.getHandle() {
        return this.handle;
    }
    
    public void SpringSocialConfig.setHandle(String handle) {
        this.handle = handle;
    }
    
    public Person SpringSocialConfig.getPerson() {
        return this.person;
    }
    
    public void SpringSocialConfig.setPerson(Person person) {
        this.person = person;
    }
    
    public String SpringSocialConfig.getUrl() {
        return this.url;
    }
    
    public void SpringSocialConfig.setUrl(String url) {
        this.url = url;
    }
    
    public AsksgUser SpringSocialConfig.getCreatedBy() {
        return this.createdBy;
    }
    
    public void SpringSocialConfig.setCreatedBy(AsksgUser createdBy) {
        this.createdBy = createdBy;
    }
    
}
