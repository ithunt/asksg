package edu.rit.asksg.domain;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Twitter extends Service {

    @Resource(name = "twitterTemplate")
    transient org.springframework.social.twitter.api.Twitter twitter;

    private static final transient Logger logger = LoggerFactory.getLogger(Twitter.class);

    @Override
    public List<Conversation> fetchNewContent() {

        final TimelineOperations timelineOperations = this.twitter.timelineOperations();
        List<Tweet> tweets = timelineOperations.getMentions();

        List<Conversation> convos = new ArrayList<Conversation>();

        for(Tweet tweet : tweets) {
            Message m = new Message();
            m.setAuthor(tweet.getFromUser());
            m.setPosted(true);
            m.setContent(tweet.getText());

            m.setUrl(tweet.getSource());

            m.setCreated(new LocalDateTime(tweet.getCreatedAt()));

            Conversation c = new Conversation();
            m.setConversation(c); //why is this causing serialization errors?

            logger.debug("New Tweet:" + m.toString());

            Set<Message> messages = new HashSet<Message>();
            messages.add(m);
            c.setMessages(messages);
            c.setProvider(this);
            convos.add(c);
        }

        return convos;
    }

    @Override
    public boolean postContent(Message message) {
        return super.postContent(message);
    }

    @Override
    public boolean authenticate() {
        return super.authenticate();
    }

    @Override
    public boolean isAuthenticated() {
        return super.isAuthenticated();
    }
}
