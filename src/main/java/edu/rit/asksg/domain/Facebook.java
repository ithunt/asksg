package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.dataio.SubscriptionProvider;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import flexjson.JSON;
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
import java.util.Collection;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Facebook extends Service implements ContentProvider, SubscriptionProvider {

	private static final transient Logger logger = LoggerFactory.getLogger(Facebook.class);

	@JSON(include = false)
	public List<Conversation> getNewContent() {
		final org.springframework.social.facebook.api.Facebook facebookApi = getFacebookApi();
		return parseFacebookFeed(facebookApi.feedOperations().getFeed(), facebookApi);
	}

	@JSON(include = false)
	public List<Conversation> getContentSince(LocalDateTime datetime) {
		final org.springframework.social.facebook.api.Facebook facebookApi = getFacebookApi();
		final List<Post> posts = facebookApi.feedOperations().getFeed();

        //filter out posts by date
        List<Post> filtered = new ArrayList<Post>();
        for(Post post : posts) {
            if(datetime.isAfter(new LocalDateTime(post.getCreatedTime()))) {
                filtered.add(post);
            }
        }

        return parseFacebookFeed(filtered, facebookApi);
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

	@JSON(include = false)
	private org.springframework.social.facebook.api.Facebook getFacebookApi() {
		//todo make safe
		return (org.springframework.social.facebook.api.Facebook) ((SpringSocialConfig) this.getConfig()).getApiBinding();
	}

    @JSON(include = false)
    public Collection<Conversation> getContentFor(SocialSubscription socialSubscription) {
        final org.springframework.social.facebook.api.Facebook facebookApi = getFacebookApi();
        return parseFacebookFeed(facebookApi.feedOperations().getFeed(socialSubscription.getHandle()), facebookApi);
    }


    protected List<Conversation> parseFacebookFeed(List<Post> posts, org.springframework.social.facebook.api.Facebook facebookApi) {
        final List<Conversation> conversations = new ArrayList<Conversation>();
        for (Post post : posts) {

            Message message = new Message();
            Conversation conversation = new Conversation(message);
            message.setConversation(conversation);
            message.setContent(post.getMessage());
            message.setAuthor(facebookApi.userOperations().getUserProfile(post.getFrom().getId()).getName());
            message.setCreated(new LocalDateTime(post.getCreatedTime()));

            for (Comment comment : post.getComments()) {
                Message commentMsg = new Message();
                commentMsg.setConversation(conversation);
                commentMsg.setContent(comment.getMessage());
                commentMsg.setAuthor(facebookApi.userOperations().getUserProfile(comment.getFrom().getId()).getName());
                commentMsg.setCreated(new LocalDateTime(comment.getCreatedTime()));
                conversation.getMessages().add(commentMsg);
            }
            conversations.add(conversation);
        }
        return conversations;

    }

}
