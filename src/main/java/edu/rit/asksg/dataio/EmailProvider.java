package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.service.ConversationService;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: ian
 * Date: 1/24/13
 * Time: 5:30 PM
 */

@Component
public class EmailProvider implements ContentProvider {

    final public static Logger logger = LoggerFactory.getLogger(EmailProvider.class);

    @Autowired
    ConversationService conversationService;

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

    public void receive(MimeMessage mimeMessage) {

        logger.debug("Received new MimeMessage... Parsing");
        Message m = new Message();

        try {
            final String sender = mimeMessage.getFrom()[0].toString();

            m.setAuthor( (sender.contains("<")? sender.substring(sender.indexOf('<')+1, sender.indexOf('>')) : sender));

            if(mimeMessage.getContent() instanceof MimeMultipart) {
                MimeMultipart body = (MimeMultipart)mimeMessage.getContent();

                for(int i = 0; i < body.getCount(); i++) {
                    BodyPart part = body.getBodyPart(i);
                    if(part.isMimeType("text/plain")) {
                        m.setContent(mimeMessage.getSubject() + " - " + part.getContent() );
                        break;
                    }
                }
            }

            logger.debug("MimeMessage from:" + m.getAuthor() + " - " + m.getContent());
        } catch (MessagingException e) {
            logger.error(e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        Conversation c = new Conversation();
        Set<Message> messages = new HashSet<Message>();
        m.setConversation(c);
        messages.add(m);
        c.setMessages(messages);

        conversationService.saveConversation(c);

    }

}
