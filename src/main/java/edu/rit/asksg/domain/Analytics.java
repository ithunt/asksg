package edu.rit.asksg.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Analytics {

    private Double sentimentScore;

    @OneToOne(cascade = CascadeType.ALL)
    private Message message;
}
