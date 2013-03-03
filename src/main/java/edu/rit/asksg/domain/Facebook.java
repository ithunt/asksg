package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.social.facebook.api.Comment;
import org.springframework.social.facebook.api.Post;

import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Facebook extends Service implements ContentProvider {

	private static final transient Logger logger = LoggerFactory.getLogger(Facebook.class);

	public List<Conversation> getNewContent() {
		// Right now this only will get the posts on the wall of the authenticated user, but
		// it's easy enough to tell getFeed to get posts from other users as well/instead.
		List<Conversation> conversations = new ArrayList<Conversation>();
		final org.springframework.social.facebook.api.Facebook facebookApi = getFacebookApi();
		List<Post> posts = facebookApi.feedOperations().getFeed();
		for (Post post : posts) {
			Conversation conversation = new Conversation();
			Message message = new Message();
			message.setConversation(conversation);
			message.setContent(post.getMessage());
			message.setAuthor(facebookApi.userOperations().getUserProfile(post.getFrom().getId()).getName());
			message.setCreated(new LocalDateTime(post.getCreatedTime()));
			conversation.getMessages().add(message);
			for (Comment comment : post.getComments()) {
				Message commentMsg = new Message();
				commentMsg.setConversation(conversation);
				commentMsg.setContent(comment.getMessage());
				commentMsg.setAuthor(facebookApi.userOperations().getUserProfile(comment.getFrom().getId()).getName());
				commentMsg.setCreated(new LocalDateTime(comment.getCreatedTime()));
			}
			conversations.add(conversation);
		}
		return conversations;
	}

	public List<Conversation> getContentSince(LocalDateTime datetime) {
		// Right now this only will get the posts on the wall of the authenticated user, but
		// it's easy enough to tell getFeed to get posts from other users as well/instead.
		List<Conversation> conversations = new ArrayList<Conversation>();
		final org.springframework.social.facebook.api.Facebook facebookApi = getFacebookApi();
		List<Post> posts = facebookApi.feedOperations().getFeed();
		for (Post post : posts) {
			if (!datetime.isBefore(new LocalDateTime(post.getCreatedTime()))) {
				continue;
			}
			Conversation conversation = new Conversation();
			Message message = new Message();
			message.setConversation(conversation);
			message.setContent(post.getMessage());
			message.setAuthor(facebookApi.userOperations().getUserProfile(post.getFrom().getId()).getName());
			message.setCreated(new LocalDateTime(post.getCreatedTime()));
			conversation.getMessages().add(message);
			for (Comment comment : post.getComments()) {
				Message commentMsg = new Message();
				commentMsg.setConversation(conversation);
				commentMsg.setContent(comment.getMessage());
				commentMsg.setAuthor(facebookApi.userOperations().getUserProfile(comment.getFrom().getId()).getName());
				commentMsg.setCreated(new LocalDateTime(comment.getCreatedTime()));
			}
			conversations.add(conversation);
		}
		return conversations;
	}

	public boolean postContent(Message message) {
		final org.springframework.social.facebook.api.Facebook facebookApi = getFacebookApi();
		try {
			facebookApi.feedOperations().post(facebookApi.userOperations().getUserProfile().getId(), message.getContent());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean authenticate() {
		return super.authenticate();
	}

	public boolean isAuthenticated() {
		return super.isAuthenticated();
	}

	private org.springframework.social.facebook.api.Facebook getFacebookApi() {
		//todo make safe
		return (org.springframework.social.facebook.api.Facebook) ((SpringSocialConfig)this.getConfig()).getApiBinding();
	}

}
