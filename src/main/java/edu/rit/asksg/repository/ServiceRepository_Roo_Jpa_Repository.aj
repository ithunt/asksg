// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Service;
import edu.rit.asksg.repository.ServiceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect ServiceRepository_Roo_Jpa_Repository {
    
    declare parents: ServiceRepository extends JpaRepository<Service, Long>;
    
    declare parents: ServiceRepository extends JpaSpecificationExecutor<Service>;
    
    declare @type: ServiceRepository: @Repository;
    
}
