package edu.rit.asksg.web;

import edu.rit.asksg.domain.Twitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Twitter.class)
@Controller
@RequestMapping("/twitters")
public class TwitterController {
}