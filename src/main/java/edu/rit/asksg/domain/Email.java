package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.service.ConversationService;
import edu.rit.asksg.service.IdentityService;
import flexjson.JSON;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Email extends Service implements ContentProvider {

    private static final transient Logger logger = LoggerFactory.getLogger(Email.class);

    @JSON(include = false)
    @Override
    public List<Conversation> getNewContent() {
        return getInbox(getConfig());
    }

    @JSON(include = false)
    @Override
    public List<Conversation> getContentSince(LocalDateTime datetime) {
        return null;
    }

    @Override
    public boolean postContent(Message message) {
        return sendMail(message);
    }


    protected boolean sendMail(Message message) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp." + getConfig().getHost());
        properties.put("mail.smtp.port", "587");


        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(getConfig().getUsername(), getConfig().getPassword());
                    }
                });
        session.setDebug(false);

        try {
            MimeMessage m = new MimeMessage(session);
            m.setFrom(new InternetAddress(getConfig().getUsername()));
            m.setRecipients(javax.mail.Message.RecipientType.TO,
                    InternetAddress.parse(message.getConversation().getRecipient()));
            m.setSubject("Your Response from SG");
            m.setText(message.getContent());
            Transport.send(m);

        } catch (MessagingException e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }


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


    public Conversation makeConversation(javax.mail.Message mimeMessage) {
        Message message = new Message();

        try {
            final String sender = mimeMessage.getFrom()[0].toString();

            //Look for email address. Sender can be of format: Jon Doe <jd@gmail.com>

            Identity identity = getIdentityService().findOrCreate(
					(sender.contains("<") ? sender.substring(sender.indexOf('<') + 1, sender.indexOf('>')) : sender));
			message.setIdentity(identity);

            if (mimeMessage.getContent() instanceof MimeMultipart) {
                MimeMultipart body = (MimeMultipart) mimeMessage.getContent();

                for (int i = 0; i < body.getCount(); i++) {
                    BodyPart part = body.getBodyPart(i);
                    if (part.isMimeType("text/plain")) {
                        message.setContent(mimeMessage.getSubject() + " - " + part.getContent());
                        break;
                    }
                }
            }

            message.setCreated(new LocalDateTime(mimeMessage.getReceivedDate()));

            logger.debug("MimeMessage from:" + message.getIdentity().getName() + " - " + message.getContent());
        } catch (MessagingException e) {
            logger.error(e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

		LocalDateTime now = LocalDateTime.now();

		Conversation conversation;

		conversation = getConversationService().findConversationByRecipientSince(message.getAuthor(), now.minusWeeks(1));
		if (conversation == null) {
			conversation = new Conversation(message);
			conversation.setCreated(message.getCreated());
		}
		message.setConversation(conversation);
		conversation.getMessages().add(message);
		conversation.setModified(message.getCreated());

        try {
			conversation.setSubject(mimeMessage.getSubject());
        } catch (MessagingException e) {
			conversation.setSubject("(no subject)");
        }
        return conversation;
    }

    @JSON(include = false)
    public List<Conversation> getInbox(ProviderConfig config) {


        Properties props = System.getProperties();

        props.setProperty("mail.store.protocol", "imaps");

        //to return
        List<Conversation> conversations = new ArrayList<Conversation>();

        Store store = null;

        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(false);
            store = session.getStore("imaps");

            /** USERNAME AND PASSWORD */
            store.connect("imap." + config.getHost(), config.getUsername(), config.getPassword());
            //store.connect("imap.", "ritasksg@gmail.com", "allHailSpring");

            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            javax.mail.Message messages[] = inbox.search(ft);
            for (javax.mail.Message message : messages) {

                try {

                    Conversation c = makeConversation(message);
                    c.setService(this);
                    conversations.add(c);

                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return conversations;
    }


}
