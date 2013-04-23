package edu.rit.asksg.web;

import com.google.common.base.Optional;
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
import org.springframework.web.context.request.WebRequest;

import javax.swing.text.html.Option;
import java.util.List;

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


    @RequestMapping(value = "/csv")
    public ResponseEntity<String> buildCsv() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>(analyticsService.buildCSV(LocalDateTime.now().minusWeeks(1), LocalDateTime.now()), headers, HttpStatus.OK);

    }

    @RequestMapping(value = "/wordcount")
    public ResponseEntity<String> getWordCountsForDates(WebRequest params) {
        String s = params.getParameter("since");
        String u = params.getParameter("until");
        LocalDateTime since = null;
        LocalDateTime until = null;

        //todo: is failed parse just null?
        if(s != null) since = LocalDateTime.parse(s);
        if(s != null) until = LocalDateTime.parse(u);

        if(since == null) since = LocalDateTime.now().minusWeeks(1);
        if(until == null) until = LocalDateTime.now();

        List<GraphData> data = analyticsService.getGraphDataInRange(since, until);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        return new ResponseEntity<String>(GraphData.toJsonArray(data), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/count")
    public ResponseEntity<String> getTest() {

        for(Service s : providerService.findAllServices()) {
            logger.info("Counting " + s.getName() + " words");


            LocalDateTime since1 = new LocalDateTime();
            LocalDateTime since2 = new LocalDateTime();
            LocalDateTime since3 = new LocalDateTime();

            since1 = since1.minusDays(1);
            since2 = since2.minusDays(2);
            since3 = since3.minusDays(3);

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

        Topic t = new Topic("rit");
        t.getWords().add("#rit");

        Topic t2 = new Topic("destler");

        Topic t3 = new Topic("elections");
        t3.getWords().add("vote");
        t3.getWords().add("election");

        Topic t4 = new Topic("debates");
        t4.getWords().add("#sgdebates");
        t4.getWords().add("debate");

        Topic t5 = new Topic("sustainability");
        t5.getWords().add("#sustainability");
        t5.getWords().add("@ritgreen");

        topicRepository.save(t);
        topicRepository.save(t2);
        topicRepository.save(t3);
        topicRepository.save(t4);
        topicRepository.save(t5);



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
