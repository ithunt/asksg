// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.repository;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect UserRepository_Roo_Jpa_Repository {
    
    declare parents: UserRepository extends JpaRepository<AsksgUser, Long>;
    
    declare parents: UserRepository extends JpaSpecificationExecutor<AsksgUser>;
    
    declare @type: UserRepository: @Repository;
    
}
