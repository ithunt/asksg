package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.dataio.MailGateway;
import edu.rit.asksg.service.ConversationService;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Email extends Service implements ContentProvider {

    private static final transient Logger logger = LoggerFactory.getLogger(Email.class);

    @Autowired
    transient ConversationService conversationService;

    @Resource(name = "mailMessage")
    transient MailMessage mailMessage;

    @Autowired
    transient MailGateway mailGateway;

    @Override
    public List<Conversation> fetchNewContent() {
        //get unread imap messages that aren't picked up by the channel listener.. how?
        //javamail api?

        return null;
    }

    @Override
    public List<Conversation> fetchContentSince(LocalDateTime datetime) {
        return null;
    }

    @Override
    public boolean postContent(Message message) {
        mailGateway.sendMail(message);
        return true;
    }

    @Override
    public boolean authenticate() {
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    public MailMessage transform(Message m) {
        if(m == null) return null;

        mailMessage.setTo( m.getRecipient() );

        mailMessage.setSubject("Your Response from SG");

        mailMessage.setSentDate( new Date(0));

        mailMessage.setText( m.getContent());

        return mailMessage;
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

        c.setProvider(this);

        conversationService.saveConversation(c);

    }

}
