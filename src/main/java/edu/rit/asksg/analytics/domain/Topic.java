package edu.rit.asksg.analytics.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Topic {

    private String name;

    @ElementCollection
    private Set<String> words;

}
