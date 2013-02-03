package edu.rit.asksg.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;

// For twitter posting nonsense
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.message.GenericMessage;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

@RequestMapping("/testtwitter/**")
@Controller
public class TestTwitterController {

	private static final Logger logger = LoggerFactory.getLogger(TestTwitterController.class);

	private final Twitter twitter;

	@Autowired
	public TestTwitterController(Twitter twitter) {
		this.twitter = twitter;
	}

	@RequestMapping(value = "/test")
    public ResponseEntity<String> test(@RequestParam("id") String screenName) {
    	TimelineOperations timelineOps = twitter.timelineOperations();

    	// need to invoke getMentions(); - can parameterize this if necessary
    	/*
 java.util.List<Tweet>	getMentions() 
          Retrieve the 20 most recent tweets that mention the authenticated user.
 java.util.List<Tweet>	getMentions(int page, int pageSize) 
          Retrieve tweets that mention the authenticated user.
 java.util.List<Tweet>	getMentions(int page, int pageSize, long sinceId, long maxId) 
          Retrieve tweets that mention the authenticated user.
    	*/

		//List<Tweet> results = timelineOps.getUserTimeline(screenName);
          List<Tweet> results = timelineOps.getMentions();
		logger.info("Found Twitter timeline for :" + screenName + " adding " + results.size()
				+ " tweets to model");
		logger.info(results.get(0).getText());
		HttpHeaders headers = new HttpHeaders();
        headers.set("content_type", "text/plain");
		return new ResponseEntity<String>("Can you see tweets? " + results.get(0).getText(), headers, HttpStatus.OK);
    }

    // This way didn't work...
    @RequestMapping(value = "/post")
    public ResponseEntity<String> test() {
    	ApplicationContext context = new ClassPathXmlApplicationContext("./twitter-outbound.xml", TestTwitterController.class);
        MessageChannel input = context.getBean("twitterOutbound", MessageChannel.class);
        Message message = new GenericMessage(Calendar.getInstance().getTime()+" @ New Message from PHONZBM using Spring Integration Module");
        input.send(message);
        return new ResponseEntity<String>("It should work now...", HttpStatus.OK);
	}
}
