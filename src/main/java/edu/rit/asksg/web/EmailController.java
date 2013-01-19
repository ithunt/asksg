package edu.rit.asksg.web;

import edu.rit.asksg.domain.Email;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Email.class)
@Controller
@RequestMapping("/emails")
public class EmailController {
}
