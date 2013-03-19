package edu.rit.asksg.service;

import edu.rit.asksg.domain.UserRole;

public class RoleServiceImpl implements RoleService {

	@Override
	public UserRole findUserRole(String name) {
		return roleRepository.findByName(name);
	}
}
