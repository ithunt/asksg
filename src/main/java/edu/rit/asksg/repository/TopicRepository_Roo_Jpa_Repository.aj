// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.repository;

import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.repository.TopicRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect TopicRepository_Roo_Jpa_Repository {
    
    declare parents: TopicRepository extends JpaRepository<Topic, Long>;
    
    declare parents: TopicRepository extends JpaSpecificationExecutor<Topic>;
    
    declare @type: TopicRepository: @Repository;
    
}
