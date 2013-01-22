package edu.rit.asksg.web;

import edu.rit.asksg.domain.Twilio;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Twilio.class)
@Controller
@RequestMapping("/twilios")
public class TwilioController {

    @RequestMapping(value = "/receive")
    public ResponseEntity<String> receiveSMS() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/xml");
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", headers, HttpStatus.OK);

        return responseEntity;
    }
}
