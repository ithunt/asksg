// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import javax.persistence.*;

privileged aspect Conversation_Roo_Jpa_Entity {

    declare @type: Conversation:@Entity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Conversation.id;

    @Version
    @Column(name = "version")
    private Integer Conversation.version;

    public Long Conversation.getId() {
        return this.id;
    }

    public void Conversation.setId(Long id) {
        this.id = id;
    }

    public Integer Conversation.getVersion() {
        return this.version;
    }

    public void Conversation.setVersion(Integer version) {
        this.version = version;
    }

}
