package edu.rit.asksg.analytics;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Facebook;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import edu.rit.asksg.repository.FacebookRepository;
import edu.rit.asksg.repository.FacebookRepositoryImpl;
import edu.rit.asksg.service.FacebookServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FacebookIntegrationTest {

	final private String fbApiKey = "350631378375337";
	final private String fbAuthToken = "be86bd629380541d0aba9c9df5524fdb";
	final private String fbOauthToken = "AAAEZB5brBUqkBAC0QPMdk8cK5MRgjURweEYyu3sbEIp0NZBUoatnqPLsopBbaVZALtRubN1ADHOWgsb6ZB3xD1S1q68TvZCM50pGOQFNeEAZDZD";
	private SpringSocialConfig fbConfig;

	@Before
	public void setup() {
		fbConfig = mock(SpringSocialConfig.class);
		when(fbConfig.getConsumerKey()).thenReturn(fbApiKey);
		when(fbConfig.getConsumerSecret()).thenReturn(fbAuthToken);
		when(fbConfig.getAccessTokenSecret()).thenReturn(fbOauthToken);
	}

	@Test
	public void getConversationsWithoutAuthing() {
		Facebook facebook = new Facebook();
		facebook.setConfig(fbConfig);
		List<Conversation> convos = facebook.getNewContent();
		assertTrue(convos.size() != 0);
	}
}
