// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.Twilio;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Twilio_Roo_Jpa_Entity {
    
    declare @type: Twilio: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Twilio.id;
    
    @Version
    @Column(name = "version")
    private Integer Twilio.version;
    
    public Long Twilio.getId() {
        return this.id;
    }
    
    public void Twilio.setId(Long id) {
        this.id = id;
    }
    
    public Integer Twilio.getVersion() {
        return this.version;
    }
    
    public void Twilio.setVersion(Integer version) {
        this.version = version;
    }
    
}
