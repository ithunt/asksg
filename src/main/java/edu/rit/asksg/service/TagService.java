package edu.rit.asksg.service;

import edu.rit.asksg.domain.Tag;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {Tag.class})
public interface TagService {
	public Tag findByName(String name);
}
