package edu.rit.asksg.web;

import edu.rit.asksg.domain.ProviderConfig;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ProviderConfig.class)
@Controller
@RequestMapping("/providerconfigs")
public class ConfigController {
}
