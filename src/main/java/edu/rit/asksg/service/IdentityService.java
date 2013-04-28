package edu.rit.asksg.service;

import com.google.common.base.Optional;
import edu.rit.asksg.domain.Identity;
import edu.rit.asksg.domain.Tag;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {Identity.class})
public interface IdentityService {
	Optional<Identity> searchIdentity(String query);

	boolean isIdentity(String candidate);

	Identity findOrCreate(String name);
}
