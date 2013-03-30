package edu.rit.asksg.service;

import edu.rit.asksg.domain.Facebook;
import org.springframework.stereotype.Service;

@Service
public interface FacebookService {

	public String makeAccessTokenRequest(Facebook facebook);

}
