package edu.rit.asksg.service;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Reddit;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.domain.config.RedditConfig;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ian
 * Date: 4/4/13
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedditIntegrationTest {

    protected Reddit reddit;


    @Before
    public void setup() {
        this.reddit = new Reddit();
        ProviderConfig config = new ProviderConfig();
        config.setIdentifier("rit");
        reddit.setConfig(config);
    }


    @Test
    public void testRedditDateParse() {
       List<Conversation> convos = reddit.getNewContent();

       Conversation c = convos.get(0);
        assertTrue( c.getMessages().get(0).getCreated().isAfter(LocalDateTime.parse("2013-01-01")));


    }




}
