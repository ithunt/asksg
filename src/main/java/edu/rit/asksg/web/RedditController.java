package edu.rit.asksg.web;

import edu.rit.asksg.domain.Reddit;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Reddit.class)
@Controller
@RequestMapping("/reddits")
public class RedditController {
}
