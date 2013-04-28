package edu.rit.asksg.service;

import edu.rit.asksg.domain.AsksgUser;
import org.springframework.roo.addon.layers.service.RooService;
import org.springframework.security.core.userdetails.UserDetails;

@RooService(domainTypes = {edu.rit.asksg.domain.AsksgUser.class})
public interface UserService {
	public AsksgUser loadUserByUsername(String username);
}
