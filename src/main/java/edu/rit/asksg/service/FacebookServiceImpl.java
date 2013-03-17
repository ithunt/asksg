package edu.rit.asksg.service;

import edu.rit.asksg.domain.Facebook;
import edu.rit.asksg.repository.FacebookRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FacebookServiceImpl implements FacebookService {

	@Autowired
	private FacebookRepositoryImpl facebookRepository;

	@Override
	public String makeAccessTokenRequest(Facebook facebook) {
		return facebookRepository.makeAccessTokenRequest(facebook);
	}
}
