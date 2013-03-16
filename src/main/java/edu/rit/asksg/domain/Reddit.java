package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.dataio.SubscriptionProvider;
import flexjson.JSON;
import org.joda.time.LocalDateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Reddit extends Service implements ContentProvider, SubscriptionProvider {

    private static final transient Logger logger = LoggerFactory.getLogger(Reddit.class);

    @JSON(include = false)
    public List<Conversation> getNewContent() {


        List<Conversation> convos = new ArrayList<Conversation>();

        try {
            convos = getRedditUrl("http://www.reddit.com/r/" + this.getConfig().getIdentifier());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return convos;

    }

    @JSON(include = false)
    protected List<Conversation> getRedditUrl(String url) throws IOException, ParseException {

        URL jsonUrl = new URL(url + "/.json");


        HttpURLConnection connection = (HttpURLConnection)jsonUrl.openConnection();
        connection.setRequestMethod("GET");

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new BufferedReader(new InputStreamReader(
                connection.getInputStream())).readLine());


        JSONObject jsonObject = (JSONObject) object;
        JSONArray posts = (JSONArray)((JSONObject)jsonObject.get("data")).get("children");


        final List<Conversation> conversations = new ArrayList<Conversation>();

        for(Object o : posts) {
            JSONObject post = (JSONObject)((JSONObject)o).get("data");
            try {
                conversations.add(new Conversation(parseMessage(post)));
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }

        return conversations;

    }

    protected Message parseMessage(JSONObject post) {
        Message m = new Message();

        String title = (String)post.get("title");

        if(post.containsKey("selftext")) {
            m.setContent(title + " - " + post.get("selftext"));
        } else {
            m.setContent(title + " - " + post.get("url"));
        }

        m.setUrl("http://reddit.com" + post.get("permalink"));
        m.setAuthor((String)post.get("author"));

       // m.setCreated( new LocalDateTime(post.get("created_utc")) );
        m.setCreated(new LocalDateTime());

        return m;

    }

    @Override
    public Collection<Conversation> getContentFor(SocialSubscription socialSubscription) {
        return null;
    }
}
