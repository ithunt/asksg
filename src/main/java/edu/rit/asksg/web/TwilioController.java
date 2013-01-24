package edu.rit.asksg.web;

import edu.rit.asksg.dataio.TwilioProvider;
import edu.rit.asksg.domain.Twilio;
import edu.rit.asksg.domain.TwilioSmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Twilio.class)
@Controller
@RequestMapping("/twilios")
public class TwilioController {

	@Autowired
	TwilioProvider provider;

	@RequestMapping(value = "/receive")
	public ResponseEntity<String> receiveSMS(TwilioSmsRequest request) {
		provider.handleMessage(request);
		//TODO this needs to return a "TwiML" XML document for Twilio
		return null;
	}
}
