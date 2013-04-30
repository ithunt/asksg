package edu.rit.asksg.service;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Person;
import edu.rit.asksg.domain.Twilio;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.domain.config.TwilioConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwilioIntegrationTest {

    final private String testAccountSid = "AC932da9adfecf700aba37dba458fc9621";
    final private String testAuthenticationToken = "9cda6e23aa46651c9759492b625e3f35";
    final private String twilioPhoneNumber = "5852865275";
    private TwilioConfig config;

    @Before
    public void setup() {
        config = mock(TwilioConfig.class);
        when(config.getUsername()).thenReturn(testAccountSid);
        when(config.getAuthenticationToken()).thenReturn(testAuthenticationToken);
        when(config.getPhoneNumber()).thenReturn(twilioPhoneNumber);
    }

    @Test
    @Ignore
    public void testIntegrationWithTwilio() {
        Twilio twilio = new Twilio();
        twilio.setConfig((ProviderConfig) config);
        Message message = new Message();
        Conversation c = new Conversation(message);
        message.setConversation(c);
        message.setContent("Test message to Twilio...");
		Person identity = new Person();
		identity.setPhoneNumber("6109990037");
        message.setIdentity(identity);
        assertTrue(twilio.postContent(message));
    }

}
