package edu.rit.asksg.service;

import edu.rit.asksg.domain.Tag;

public class TagServiceImpl implements TagService {
	@Override
	public Tag findByName(String name) {
		return this.tagRepository.findByName(name);
	}
}
