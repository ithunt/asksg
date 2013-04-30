package edu.rit.asksg.domain;

import com.google.common.collect.Iterables;
import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.dataio.SubscriptionProvider;
import edu.rit.asksg.service.IdentityService;
import flexjson.JSON;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Reddit extends Service implements ContentProvider, SubscriptionProvider {

    public static final String REDDIT_DOMAIN = "http://www.reddit.com";

    private static final transient Logger logger = LoggerFactory.getLogger(Reddit.class);

    @JSON(include = false)
    public List<Conversation> getNewContent() {
        List<Conversation> convos = new ArrayList<Conversation>();

        try {
            convos = getRedditPosts(REDDIT_DOMAIN + "/r/" + this.getConfig().getIdentifier());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return convos;

    }

    @JSON(include = false)
    protected List<Conversation> getRedditPosts(String url) throws IOException, ParseException {

        JSONArray posts = getPostsFromSubreddit(url);

        final List<Conversation> conversations = new ArrayList<Conversation>();

        for (Object o : posts) {
            JSONObject post = (JSONObject) ((JSONObject) o).get("data");

            try {
                Conversation c = new Conversation(parsePost(post));
                if (c.getMessages() != null && !c.getMessages().isEmpty())
                    c.setCreated(c.getMessages().get(0).getCreated());
                c.setService(this);
                c.setSubject((String) post.get("title"));
                attachComments(c);
                conversations.add(c);
            } catch (Exception e) {
                logger.error("Error parsing reddit post " + e.getLocalizedMessage(), e);
            }
        }

        return conversations;
    }

    protected Conversation attachComments(Conversation c) throws IOException, ParseException {

        if (c.getMessages() == null || c.getMessages().size() != 1) return c;

        Message m = Iterables.get(c.getMessages(), 0);
        List<Message> messages = new ArrayList<Message>();
        messages.add(m);

        for (Object comment : getTopLevelComments(m.getUrl())) {
            try {
                JSONObject obj = (JSONObject) ((JSONObject) comment).get("data");
                Message parsedComment = parseComment(obj);
                parsedComment.setConversation(c);
                messages.add(parsedComment);
            } catch (Exception e) {
                logger.error("Error parsing reddit comment - " + e.getLocalizedMessage(), e);
            }
        }
        c.setMessages(messages);
        return c;
    }

    protected Message parsePost(JSONObject post) {
        Message message = new Message();

        if (post.containsKey("selftext")) {
            message.setContent((String) post.get("selftext"));
        } else {
            message.setContent((String) post.get("url"));
        }

        message.setUrl("http://reddit.com" + post.get("permalink"));
		Identity identity = getIdentityService().findOrCreate((String)post.get("author"));
        message.setIdentity(identity);

        message.setCreated(new LocalDateTime(((Double) post.get("created_utc")).longValue() * 1000L, DateTimeZone.UTC));

        return message;
    }

    protected Message parseComment(JSONObject comment) {
        Message message = new Message();
		Identity identity = getIdentityService().findOrCreate((String)comment.get("author"));
		message.setIdentity(identity);
        message.setContent((String) comment.get("body"));
        message.setCreated(new LocalDateTime(((Double) comment.get("created_utc")).longValue() * 1000L, DateTimeZone.UTC));

        return message;
    }

    @JSON(include = false)
    protected JSONArray getTopLevelComments(String permalink) throws IOException, ParseException {
        JSONArray arr = (JSONArray) getJSONResponse(new URL(permalink + "/.json"));
        JSONArray retVal = new JSONArray();

        if (arr.size() > 1) {
            try {

                retVal = (JSONArray) ((JSONObject) ((JSONObject) arr.get(1)).get("data")).get("children");
            } catch (Exception e) {
                logger.error("Error in getting top level comments" + e.getLocalizedMessage(), e);
            }
        }
        return retVal;
    }

    @JSON(include = false)
    protected JSONArray getPostsFromSubreddit(String subreddit) throws IOException, ParseException {
        JSONObject object = (JSONObject) getJSONResponse(new URL(subreddit + "/.json"));
        return (JSONArray) ((JSONObject) object.get("data")).get("children");
    }

    @JSON(include = false)
    protected Object getJSONResponse(URL url) throws IOException, ParseException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new BufferedReader(new InputStreamReader(
                connection.getInputStream())).readLine());

        return object;
    }


    @Override
    public Collection<Conversation> getContentFor(SocialSubscription socialSubscription) {

        List<Conversation> conversations = new ArrayList<Conversation>();
        try {
            conversations = getRedditPosts(REDDIT_DOMAIN + "/r/" + socialSubscription.getHandle());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return conversations;
    }
}