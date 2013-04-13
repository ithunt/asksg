package edu.rit.asksg.web;

import edu.rit.asksg.analytics.WordCounter;
import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import edu.rit.asksg.service.AnalyticsService;
import edu.rit.asksg.service.ProviderService;
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
            wordCounter.work(s);
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

        Map<Topic, Integer> m = analyticsService.getAggregatedWordCount();

        List<String> words = new ArrayList<String>();
        List<Integer> counts = new ArrayList<Integer>();

        for(Map.Entry<Topic, Integer> e : m.entrySet()) {
            words.add(e.getKey().getName());
            counts.add(e.getValue());
        }

        StringBuilder sb = new StringBuilder();
        for(String w : words) {
            sb.append(w + "\t");
        }
        sb.append("\n");

        for(Integer i : counts) {
            sb.append(i + "\t\t");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
    }







}
