package edu.rit.asksg.web;

import edu.rit.asksg.domain.Conversation;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Conversation.class)
@Controller
@RequestMapping("/conversations")
public class ConversationController {
}
