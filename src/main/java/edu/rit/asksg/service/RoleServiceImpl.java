package edu.rit.asksg.service;

import edu.rit.asksg.domain.UserRole;
import org.springframework.transaction.annotation.Transactional;

public class RoleServiceImpl implements RoleService {

	@Override
	public UserRole findUserRole(String name) {
		return roleRepository.findByName(name);
	}
}
