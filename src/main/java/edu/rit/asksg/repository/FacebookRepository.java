package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Facebook;

public interface FacebookRepository {
	public String makeAccessTokenRequest(Facebook facebook);
}
