package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.MessagingException;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageHandler;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * User: ian
 * Date: 1/24/13
 * Time: 5:30 PM
 */


public class EmailProvider implements ContentProvider, MessageHandler {

    final public static Logger logger = LoggerFactory.getLogger(EmailProvider.class);


    @Override
    public List<Conversation> fetchNewContent() {
        return null;
    }

    @Override
    public List<Conversation> fetchContentSince(LocalDateTime datetime) {
        return null;
    }

    @Override
    public boolean postContent(Message message) {
        return false;
    }

    @Override
    public boolean authenticate() {
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    public void recieve(MimeMessage mimeMessage) {
        logger.debug("New Mime Email!");
        logger.debug(mimeMessage.toString());
    }

    public void handleMessage(org.springframework.integration.Message<?> message) throws MessagingException {
        logger.debug("New Email!");
        logger.debug(message.toString());
    }
}
