package edu.rit.asksg.analytics;

import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Twilio;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.domain.config.TwilioConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwilioIntegrationTest {

	final private String testAccountSid = "AC932da9adfecf700aba37dba458fc9621";
	final private String testAuthenticationToken = "9cda6e23aa46651c9759492b625e3f35";
	private ProviderConfig config;

	@Before
	public void setup() {
		config = mock(TwilioConfig.class);
		when(config.getUsername()).thenReturn(testAccountSid);
		when(config.getAuthenticationToken()).thenReturn(testAuthenticationToken);
	}

	@Test
	public void testIntegrationWithTwilio() {
		Twilio twilio = new Twilio();
		twilio.setConfig(config);
		Message message = new Message();
		message.setContent("Test message to Twilio...");
		message.setAuthor("6109990037");
		assertTrue(twilio.postContent(message));
	}

}
