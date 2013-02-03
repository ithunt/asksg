// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.SocialSubscription;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SocialSubscription_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager SocialSubscription.entityManager;
    
    public static final EntityManager SocialSubscription.entityManager() {
        EntityManager em = new SocialSubscription().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long SocialSubscription.countSocialSubscriptions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SocialSubscription o", Long.class).getSingleResult();
    }
    
    public static List<SocialSubscription> SocialSubscription.findAllSocialSubscriptions() {
        return entityManager().createQuery("SELECT o FROM SocialSubscription o", SocialSubscription.class).getResultList();
    }
    
    public static SocialSubscription SocialSubscription.findSocialSubscription(Long id) {
        if (id == null) return null;
        return entityManager().find(SocialSubscription.class, id);
    }
    
    public static List<SocialSubscription> SocialSubscription.findSocialSubscriptionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SocialSubscription o", SocialSubscription.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void SocialSubscription.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void SocialSubscription.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SocialSubscription attached = SocialSubscription.findSocialSubscription(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void SocialSubscription.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void SocialSubscription.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public SocialSubscription SocialSubscription.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SocialSubscription merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
