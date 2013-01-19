package edu.rit.asksg.web;

import edu.rit.asksg.domain.TopicTag;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = TopicTag.class)
@Controller
@RequestMapping("/topictags")
public class TopicTagController {
}
