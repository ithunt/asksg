package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.dataio.SubscriptionProvider;
import edu.rit.asksg.domain.config.SpringSocialConfig;
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
import org.springframework.social.twitter.api.DirectMessage;
import org.springframework.social.twitter.api.DirectMessageOperations;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Twitter extends Service implements ContentProvider, SubscriptionProvider {

    private static final transient Logger logger = LoggerFactory.getLogger(Twitter.class);

    @JSON(include = false)
    @Override
    public List<Conversation> getNewContent() {

        //Get Timeline of Authenticated User
        final org.springframework.social.twitter.api.Twitter twitterApi = getTwitterApi();
        final TimelineOperations timelineOperations = twitterApi.timelineOperations();
        final List<Tweet> tweets = timelineOperations.getHomeTimeline();
        List<Conversation> conversations = parseTweets(tweets);

        //Get Direct Messages of Authenticated user
        final DirectMessageOperations directMessageOperations = twitterApi.directMessageOperations();
        final List<DirectMessage> directMessages = directMessageOperations.getDirectMessagesReceived();
        conversations.addAll(parseDirectMessages(directMessages));

        return conversations;
    }

    @Override
    public boolean postContent(Message message) {
        final org.springframework.social.twitter.api.Twitter twitterApi = getTwitterApi();
        if (message.getConversation().getPrivateConversation()) {
            final DirectMessageOperations directMessageOperations = twitterApi.directMessageOperations();
            directMessageOperations.sendDirectMessage(message.getConversation().getRecipient(), message.getContent());
            return true;
            //todo: update the current one with the id?
        } else {

            final TimelineOperations timelineOperations = twitterApi.timelineOperations();
            final String tweet =
                    ((message.getConversation().getRecipient() != null) ? "@" + message.getConversation().getRecipient() + " " : "") +
                            message.getContent();

            return !(timelineOperations.updateStatus(tweet) == null);

        }
    }

    protected List<Conversation> parseDirectMessages(List<DirectMessage> messages) {

        final List<Conversation> conversations = new ArrayList<Conversation>();
        for (DirectMessage dm : messages) {
            Message message = new Message();
			Identity identity = getIdentityService().findOrCreate(dm.getSender().getName());
			message.setIdentity(identity);
			LocalDateTime created = new LocalDateTime(dm.getCreatedAt());
            message.setCreated(created);
			message.setModified(created);
            message.setContent(dm.getText());

			Conversation conversation = getConversationService().findConversationByRecipientSince(message.getAuthor(), created.minusWeeks(1));

			if (conversation == null) {
				conversation = new Conversation(message);
				conversation.setCreated(created);
			}
			conversation.setPrivateConversation(true);
            conversation.setService(this);
            conversation.setExternalId(String.valueOf(dm.getId()));
			conversation.setModified(created);
			conversation.getMessages().add(message);

            message.setConversation(conversation);

            conversations.add(conversation);
        }

        return conversations;
    }

    protected List<Conversation> parseTweets(List<Tweet> tweets) {
        final List<Conversation> conversations = new ArrayList<Conversation>();

        for (Tweet tweet : tweets) {
            Message message = new Message();
			Identity identity = getIdentityService().findOrCreate(tweet.getFromUser());
			message.setIdentity(identity);
            message.setPosted(true);
            message.setContent(tweet.getText());
            message.setUrl(tweet.getSource());
			LocalDateTime created = new LocalDateTime(tweet.getCreatedAt());
            message.setCreated(created);
			message.setModified(created);


			Conversation conversation = getConversationService().findConversationByRecipientSince(message.getAuthor(), created.minusWeeks(1));

			if (conversation == null) {
				conversation = new Conversation(message);
				conversation.setCreated(created);
			}
			conversation.setService(this);
			conversation.setExternalId(String.valueOf(tweet.getId()));
			conversation.setModified(created);
			conversation.getMessages().add(message);
            conversations.add(conversation);
        }

        return conversations;
    }

    @Override
    public boolean authenticate() {
        return super.authenticate();
    }

    @Override
    public boolean isAuthenticated() {
        return super.isAuthenticated();
    }

    @JSON(include = false)
    private org.springframework.social.twitter.api.Twitter getTwitterApi() {

        final SpringSocialConfig config = (SpringSocialConfig) this.getConfig();
        return new TwitterTemplate(config.getConsumerKey(), config.getConsumerSecret(), config.getAccessToken(), config.getAccessTokenSecret());

    }

    @JSON(include = false)
    public Collection<Conversation> getContentFor(SocialSubscription socialSubscription) {

        List<Conversation> conversations;

        //hashtag
        if (socialSubscription.getHandle().startsWith("#")) {
            final SearchOperations searchOperations = getTwitterApi().searchOperations();
            conversations = parseTweets(searchOperations.search(socialSubscription.getHandle()).getTweets());
        } else {
            final TimelineOperations timelineOperations = getTwitterApi().timelineOperations();
            final List<Tweet> tweets = timelineOperations.getUserTimeline(socialSubscription.getHandle());
            conversations = parseTweets(tweets);
        }


        return conversations;
    }
}
