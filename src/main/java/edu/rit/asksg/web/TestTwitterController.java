package edu.rit.asksg.web;

import edu.rit.asksg.domain.Twitter;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;

// For twitter posting nonsense

@RequestMapping("/testtwitter/**")
@Controller
public class TestTwitterController {

	private static final Logger logger = LoggerFactory.getLogger(TestTwitterController.class);

	@Autowired
	private ProviderService providerService;
	//this.twitter = providerService.findServiceByTypeAndIdentifierEquals(Twitter.class, "RIT_SG");
	private Twitter twitter;

	//todo: turn into actual junit tests...
	public TestTwitterController() {
	}

	@RequestMapping(value = "/test")
	public ResponseEntity<String> test(@RequestParam("id") String screenName) {
		// todo: needs to delegate into domain model - better to just turn into junit tests

		//	TimelineOperations timelineOps = twitter.timelineOperations();

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

		/*
	      List<Tweet> results = timelineOps.getMentions();
		logger.info("Found Twitter timeline for :" + screenName + " adding " + results.size()
				+ " tweets to model");
		logger.info(results.get(0).getText());

		*/

		HttpHeaders headers = new HttpHeaders();
		headers.set("content_type", "text/plain");
		return new ResponseEntity<String>("Can you see tweets? ", headers, HttpStatus.OK);
	}

	// This way didn't work...
	@RequestMapping(value = "/post")
	public ResponseEntity<String> test() {
		// todo: needs to delegate into domain model - better to just turn into junit tests

		//todo rebind outbound channel - use
	    /*
	    final DirectChannel channel = new DirectChannel();
		final StatusUpdatingMessageHandler outbound = new StatusUpdatingMessageHandler(twitterApi);
	     */
		Message message = new GenericMessage(Calendar.getInstance().getTime() + " @ New Message from PHONZBM using Spring Integration Module");
		// input.send(message);
		return new ResponseEntity<String>("It should work now...", HttpStatus.OK);
	}
}
