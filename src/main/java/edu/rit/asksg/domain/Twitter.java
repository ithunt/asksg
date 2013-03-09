package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import flexjson.JSON;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;

import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Twitter extends Service implements ContentProvider {

	private static final transient Logger logger = LoggerFactory.getLogger(Twitter.class);

	@JSON(include = false)
	@Override
	public List<Conversation> getNewContent() {
		final org.springframework.social.twitter.api.Twitter twitterApi = getTwitterApi();
		final TimelineOperations timelineOperations = twitterApi.timelineOperations();
		final List<Tweet> tweets = timelineOperations.getHomeTimeline();
		return parseTweets(tweets);
	}

	@Override
	public boolean postContent(Message message) {

		final org.springframework.social.twitter.api.Twitter twitterApi = getTwitterApi();
		final TimelineOperations timelineOperations = twitterApi.timelineOperations();
		final String tweet =
				((message.getRecipient() != null) ? "@" + message.getRecipient() + " " : "") +
						message.getContent();

		return !(timelineOperations.updateStatus(tweet) == null);
	}

	protected List<Conversation> parseTweets(List<Tweet> tweets) {
		final List<Conversation> convos = new ArrayList<Conversation>();

		for (Tweet tweet : tweets) {
			Message m = new Message();
			m.setAuthor(tweet.getFromUser());
			m.setPosted(true);
			m.setContent(tweet.getText());
			m.setUrl(tweet.getSource());
			m.setCreated(new LocalDateTime(tweet.getCreatedAt()));

			Conversation c = new Conversation(m);
			m.setConversation(c);
			c.setService(this);
			convos.add(c);

			logger.debug("New Tweet:" + m.toString());

		}
		return convos;
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
		//todo make safe
		return (org.springframework.social.twitter.api.Twitter) ((SpringSocialConfig) this.getConfig()).getApiBinding();
	}
}
