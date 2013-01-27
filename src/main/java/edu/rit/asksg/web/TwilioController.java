package edu.rit.asksg.web;

import edu.rit.asksg.dataio.TwilioProvider;
import edu.rit.asksg.domain.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@RooWebJson(jsonObject = Twilio.class)
@Controller
@RequestMapping("/twilios")
public class TwilioController {

	@Autowired
	TwilioProvider provider;

	@RequestMapping(value = "/receive", method = RequestMethod.POST, produces = "text/xml")
	public
	@ResponseBody
	TwiMLResponse receiveSMS(@RequestBody Map<String, String> request) {
		provider.handleMessage(request);

		//TODO: I'm not sure what this XML document will actually look like to Twilio.
		TwiMLResponse response = new TwiMLResponse();
		response.Sms = "Thank you for contacting ASKSG.";
		return response;
	}

	/*
	 * Used to respond to Twilio.
	 */
	private class TwiMLResponse {
		public String Sms;
	}
}
