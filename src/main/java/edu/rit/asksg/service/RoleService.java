package edu.rit.asksg.service;

import edu.rit.asksg.domain.UserRole;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {UserRole.class})
public interface RoleService {
	UserRole findUserRole(String name);
}
