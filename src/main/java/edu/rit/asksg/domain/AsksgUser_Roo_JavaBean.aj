// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.UserRole;

privileged aspect AsksgUser_Roo_JavaBean {
    
    public String AsksgUser.getUserName() {
        return this.userName;
    }
    
    public void AsksgUser.setUserName(String userName) {
        this.userName = userName;
    }
    
    public void AsksgUser.setPassword(String password) {
        this.password = password;
    }
    
    public UserRole AsksgUser.getRole() {
        return this.role;
    }
    
    public void AsksgUser.setRole(UserRole role) {
        this.role = role;
    }
    
}
