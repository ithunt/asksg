// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.analytics.domain;

import edu.rit.asksg.analytics.domain.Topic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Topic_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Topic.entityManager;
    
    public static final EntityManager Topic.entityManager() {
        EntityManager em = new Topic().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Topic.countTopics() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Topic o", Long.class).getSingleResult();
    }
    
    public static List<Topic> Topic.findAllTopics() {
        return entityManager().createQuery("SELECT o FROM Topic o", Topic.class).getResultList();
    }
    
    public static Topic Topic.findTopic(Long id) {
        if (id == null) return null;
        return entityManager().find(Topic.class, id);
    }
    
    public static List<Topic> Topic.findTopicEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Topic o", Topic.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Topic.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Topic.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Topic attached = Topic.findTopic(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Topic.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Topic.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Topic Topic.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Topic merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
