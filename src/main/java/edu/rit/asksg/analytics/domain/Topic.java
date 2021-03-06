package edu.rit.asksg.analytics.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Topic {

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> words;

    public Topic(String s) {
        this.name = s;
        words = new HashSet<String>();
        words.add(s);
    }

    public Topic(String s, String ss) {
        this.name = s;
        words = new HashSet<String>();
        words.add(s);
        words.addAll(Arrays.asList(ss.split(",")));
    }

    public Topic() {

    }

    @Override
    public Topic clone() {
        Topic t = new Topic(this.name);
        t.setWords(new HashSet<String>(this.words));
        return t;
    }



}
