package edu.rit.asksg.web;

import edu.rit.asksg.domain.Analytics;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Analytics.class)
@Controller
@RequestMapping("/analyticses")
public class AnalyticsController {
}
