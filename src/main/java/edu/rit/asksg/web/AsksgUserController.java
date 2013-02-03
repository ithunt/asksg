package edu.rit.asksg.web;

import edu.rit.asksg.domain.AsksgUser;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = AsksgUser.class)
@Controller
@RequestMapping("/asksgusers")
public class AsksgUserController {
}
