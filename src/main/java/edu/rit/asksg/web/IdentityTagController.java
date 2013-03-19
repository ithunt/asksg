package edu.rit.asksg.web;

import edu.rit.asksg.domain.IdentityTag;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = IdentityTag.class)
@Controller
@RequestMapping("/identitytags")
public class IdentityTagController {
}
