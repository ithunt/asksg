// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.ProviderConfig;
import edu.rit.asksg.domain.Service;

privileged aspect Service_Roo_JavaBean {
    
    public ProviderConfig Service.getConfig() {
        return this.config;
    }
    
    public void Service.setConfig(ProviderConfig config) {
        this.config = config;
    }
    
}
