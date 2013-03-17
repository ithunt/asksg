package edu.rit.asksg.analytics;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Facebook;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FacebookIntegrationTest {

	final private String fbAuthToken = "be86bd629380541d0aba9c9df5524fdb";
	private SpringSocialConfig fbConfig;

	@Before
	public void setup() {
		fbConfig = mock(SpringSocialConfig.class);
		when(fbConfig.getAuthenticationToken()).thenReturn(fbAuthToken);
	}

	@Test
	public void testIntegrationWithFacebook() {
		Facebook facebook = new Facebook();
		facebook.setConfig((ProviderConfig)fbConfig);
		List<Conversation> convos = facebook.getNewContent ();
		assertTrue(convos.size() != 0);
	}
}
