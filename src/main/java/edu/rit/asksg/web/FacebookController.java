package edu.rit.asksg.web;

import edu.rit.asksg.domain.Facebook;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Facebook.class)
@Controller
@RequestMapping("/facebooks")
public class FacebookController {
}
