// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.Identity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Identity_Roo_Jpa_Entity {
    
    declare @type: Identity: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Identity.id;
    
    @Version
    @Column(name = "version")
    private Integer Identity.version;
    
    public Long Identity.getId() {
        return this.id;
    }
    
    public void Identity.setId(Long id) {
        this.id = id;
    }
    
    public Integer Identity.getVersion() {
        return this.version;
    }
    
    public void Identity.setVersion(Integer version) {
        this.version = version;
    }
    
}