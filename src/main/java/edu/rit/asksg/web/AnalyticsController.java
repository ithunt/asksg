package edu.rit.asksg.web;

import edu.rit.asksg.analytics.WordCounter;
import edu.rit.asksg.analytics.domain.GraphData;
import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import edu.rit.asksg.service.AnalyticsService;
import edu.rit.asksg.service.ProviderService;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    @Log
    Logger logger;

    @Autowired
    ProviderService providerService;

    @Autowired
    WordCounter wordCounter;

    @Autowired
    WordCountRepository wordCountRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    AnalyticsService analyticsService;


    @RequestMapping(value = "/count")
    public ResponseEntity<String> getTest() {

        for(Service s : providerService.findAllServices()) {
            logger.info("Counting " + s.getName() + " words");


            LocalDateTime since1 = new LocalDateTime();
            LocalDateTime since2 = new LocalDateTime();
            LocalDateTime since3 = new LocalDateTime();

            since1 = since1.minusDays(3);
            since2 = since2.minusDays(6);
            since3 = since3.minusDays(9);

            wordCounter.work(s, since3, since2);
            wordCounter.work(s, since2, since1);
            wordCounter.work(s, since1, LocalDateTime.now());
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>("Counting...", headers, HttpStatus.OK);

    }



    @RequestMapping(value = "/topics")
    public ResponseEntity<String> topicTest() {

        Topic t = new Topic();
        t.setName("RIT");

        Set<String> words = new TreeSet<String>();
        words.add("#rit");
        words.add("rit");

        t.setWords(words);

        topicRepository.save(t);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>("#RIT", headers, HttpStatus.OK);

    }


    @RequestMapping(value = "/words")
    public ResponseEntity<String> wordCounts() {

        List<GraphData> data = analyticsService.getAggregatedWordCount();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        return new ResponseEntity<String>(GraphData.toJsonArray(data), headers, HttpStatus.OK);
    }







}
