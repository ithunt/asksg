// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Identity;
import edu.rit.asksg.repository.IdentityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect IdentityRepository_Roo_Jpa_Repository {
    
    declare parents: IdentityRepository extends JpaRepository<Identity, Long>;
    
    declare parents: IdentityRepository extends JpaSpecificationExecutor<Identity>;
    
    declare @type: IdentityRepository: @Repository;
    
}
