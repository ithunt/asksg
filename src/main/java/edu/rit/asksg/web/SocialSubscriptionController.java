package edu.rit.asksg.web;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.SocialSubscription;
import edu.rit.asksg.domain.Twitter;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

@RooWebJson(jsonObject = SocialSubscription.class)
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

        final SocialSubscription s = new SocialSubscription();
	    final String HANDLE = "RIT_SG";
        s.setHandle(HANDLE);
        s.setProvider(providerService.findServiceByTypeAndIdentifierEquals(Twitter.class, HANDLE));
        s.setUrl("https://twitter.com/RIT_SG"); //hmm https?

        socialSubscriptionService.saveSocialSubscription(s);

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("subscribed to RIT_SG twitter", headers, HttpStatus.OK);

    }
}
