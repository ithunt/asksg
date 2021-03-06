package edu.rit.asksg.web;

import edu.rit.asksg.domain.SocialSubscription;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = SocialSubscription.class)
@Controller
@RequestMapping("/socialsubscriptions")
public class SocialSubscriptionController {
}
