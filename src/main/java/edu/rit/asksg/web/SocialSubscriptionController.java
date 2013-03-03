package edu.rit.asksg.web;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.Twitter;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = SpringSocialConfig.class)
@Controller
@RequestMapping("/socialsubscriptions")
public class SocialSubscriptionController {

	@Autowired
	ProviderService providerService;
	@Log
	private Logger logger;

	//todo: consolidate seed methods
	@RequestMapping(value = "seed")
	public ResponseEntity<String> seed() {

		final Service twitter = new Twitter();
		final SpringSocialConfig config = new SpringSocialConfig();
		final String HANDLE = "RIT_SG";
		config.setIdentifier(HANDLE);
		config.setHandle(HANDLE);
		config.setUrl("https://twitter.com/RIT_SG"); //hmm https?
		twitter.setConfig(config);
		final String consumerkey = "wY0Aft0Gz410RtOqOHd7Q";
		final String consumersecret = "rMxrTP9nqPzwU6UHIQufKR23be4w4NHIqY7VbwfzU";
		final String accesstoken = "15724679-FUz0huThLIpEzm66QySG7exllaV1CWV9VqXxXeTOw";
		final String accesstokensecret = "rFTEFz8tNX71V2nCo6pDtF38LhDEfO2f692xxzQxaA";

		final TwitterTemplate twitterTemplate = new TwitterTemplate(consumerkey, consumersecret, accesstoken, accesstokensecret);
		config.setApiBinding(twitterTemplate);

		providerService.saveService(twitter);

		HttpHeaders headers = new HttpHeaders();
		headers.add("content_type", "text/plain");

		return new ResponseEntity<String>("subscribed to RIT_SG twitter", headers, HttpStatus.OK);

	}
}
