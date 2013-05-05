package edu.rit.asksg.web;

import edu.rit.asksg.domain.config.ProviderConfig;
import org.joda.time.Minutes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RooWebJson(jsonObject = ProviderConfig.class)
@Controller
@RequestMapping("/providerconfigs")
public class ConfigController {

	@RequestMapping(method = RequestMethod.POST, value = "/updateLimits")
	public ResponseEntity<String> updateLimits(@RequestParam(value = "id") String id,
											   @RequestParam(value = "maxCalls") String maxCalls,
											   @RequestParam (value = "updateFrequency") String updateFrequency) {
		ProviderConfig configToUpdate = configService.findProviderConfig(Long.parseLong(id));
		configToUpdate.setMaxCalls(Integer.parseInt(maxCalls));
		configToUpdate.setUpdateFrequency(Minutes.minutes(Integer.parseInt(updateFrequency)));

		configService.updateProviderConfig(configToUpdate);

		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
