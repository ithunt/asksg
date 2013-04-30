package edu.rit.asksg.service;


import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Email;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Person;
import edu.rit.asksg.domain.config.EmailConfig;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmailIntegrationTest {


    private EmailConfig emailConfig;
    private Email email;


    @Before
    public void setup() {
        emailConfig = new EmailConfig();
        emailConfig.setHost("gmail.com");
        emailConfig.setUsername("ritasksg@gmail.com");
        emailConfig.setPassword("allHailSpring");
        emailConfig.setIdentifier("ritasksg");

        email = new Email();
        email.setConfig(emailConfig);
    }


    @Ignore
    @Test
    public void testGetMail() {
        assertNotNull(email.getNewContent());
    }

    @Ignore
    @Test
    public void testSendMail() {
        Message message1 = new Message();
		Person identity1 = new Person();
		identity1.setEmail("vulshok@gmail.com");
		message1.setIdentity(identity1);
        Conversation c = new Conversation(message1);
        message1.setConversation(c);
	    message1.setContent("Originating input messaage");

	    Message m2 = new Message();
	    m2.setConversation(c);
	    c.getMessages().add(m2);
        m2.setContent("Test successful - " + new LocalDateTime());
		Person identity2 = new Person();
		identity2.setName("testuser");
        m2.setIdentity(identity2);

	    assertTrue(email.postContent(m2));
    }

}
