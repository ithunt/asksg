// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.analytics.domain;

import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.domain.Service;
import org.joda.time.LocalDateTime;

privileged aspect WordCount_Roo_JavaBean {
    
    public Topic WordCount.getTopic() {
        return this.topic;
    }
    
    public void WordCount.setTopic(Topic topic) {
        this.topic = topic;
    }
    
    public Service WordCount.getService() {
        return this.service;
    }
    
    public void WordCount.setService(Service service) {
        this.service = service;
    }
    
    public Long WordCount.getWordCount() {
        return this.wordCount;
    }
    
    public void WordCount.setWordCount(Long wordCount) {
        this.wordCount = wordCount;
    }
    
    public LocalDateTime WordCount.getCreated() {
        return this.created;
    }
    
    public void WordCount.setCreated(LocalDateTime created) {
        this.created = created;
    }
    
}