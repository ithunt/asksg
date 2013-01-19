package edu.rit.asksg.web;

import edu.rit.asksg.domain.Twilio;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Twilio.class)
@Controller
@RequestMapping("/twilios")
public class TwilioController {
}
