package edu.rit.asksg.analytics;

import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import edu.rit.asksg.service.ConversationService;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WordCounter {

    @Log
    Logger logger;

    @Autowired
    ConversationService conversationService;

    @Autowired
    WordCountRepository wordCountRepository;

    @Autowired
    TopicRepository topicRepository;

    @Async
    public void work(final Service service, final LocalDateTime since, final LocalDateTime until) {
        List<Conversation> conversations = conversationService.findByService(service, since, until);

        Map<String, WordCount> countMap = buildCountMapWithService(conversations, service, until);

        try {
            wordCountRepository.save(countMap.values());
        } catch (Error e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }


    //todo: Not load ALL conversations, chunk it out - page requests ala paginated conversations
    protected Map<String, WordCount> buildCountMapWithService(
            final List<Conversation> conversations,
            final Service service,
            final LocalDateTime until) {

        final Map<String, Topic> topicMap = new HashMap<String, Topic>();
        for(Topic t : topicRepository.findAll()) {
            for(String s : t.getWords()) {
                topicMap.put(s, t);
            }
        }

        Map<String, WordCount> countMap = new HashMap<String, WordCount>();
        for(Conversation c : conversations) {
            for(Message m : c.getMessages()) {
                for(String s : m.getContent().split("\\n| |\\r|;|\\(|\\)")) {
                    try {

                        //remove trailing punctuation , could be more here?
                        if(s.endsWith(".") || s.endsWith(",")) s=s.substring(0,s.length()-1);
                        s = s.toLowerCase();

                        //Only count words we care about
                        if(topicMap.containsKey(s)) {
                            logger.debug("Found topic word: " + s);
                            if(countMap.containsKey(s)) {

                                WordCount wc = countMap.get(s);
                                wc.setWordCount(wc.getWordCount() + 1L);

                            } else {

                                WordCount wc = new WordCount();
                                wc.setTopic(topicMap.get(s));
                                wc.setWordCount(1L);
                                wc.setCreated(until);
                                wc.setService(service);
                                countMap.put(s, wc);
                            }
                        }


                    } catch (Error e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        }
        return  countMap;
    }










}
