package edu.rit.asksg.web;

import edu.rit.asksg.analytics.WordCounter;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    @Log
    Logger logger;

    @Autowired
    ProviderService providerService;

    @Autowired
    WordCounter wordCounter;


    @RequestMapping(value = "/count")
    public ResponseEntity<String> getTest() {

        for(Service s : providerService.findAllServices()) {
            logger.info("Counting " + s.getName() + " words");
            wordCounter.work(s);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>("Counting...", headers, HttpStatus.OK);

    }

}
