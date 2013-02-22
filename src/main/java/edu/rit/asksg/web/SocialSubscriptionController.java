package edu.rit.asksg.web;

import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.SocialSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Resource
    Map<String, Service> providerMap;

    public static final Logger logger = LoggerFactory.getLogger(SocialSubscriptionController.class);

    @RequestMapping(value = "seed")
    public ResponseEntity<String> seed() {

        SocialSubscription s = new SocialSubscription();
        s.setHandle("RIT_SG");
        s.setProvider(providerMap.get("twitter"));
        s.setUrl("https://twitter.com/RIT_SG"); //hmm https?

        socialSubscriptionService.saveSocialSubscription(s);

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("subscribed to RIT_SG twitter", headers, HttpStatus.OK);

    }
}
