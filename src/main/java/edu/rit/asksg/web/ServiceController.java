package edu.rit.asksg.web;

import edu.rit.asksg.domain.Service;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Service.class)
@Controller
@RequestMapping("/services")
public class ServiceController {
}
