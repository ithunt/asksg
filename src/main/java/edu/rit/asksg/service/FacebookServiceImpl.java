package edu.rit.asksg.service;

import edu.rit.asksg.domain.Facebook;
import edu.rit.asksg.repository.FacebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FacebookServiceImpl implements FacebookService {

	@Autowired
	private FacebookRepository facebookRepository;

	@Override
	public String makeAccessTokenRequest(final Facebook facebook) {
		return facebookRepository.makeAccessTokenRequest(facebook);
	}
}
