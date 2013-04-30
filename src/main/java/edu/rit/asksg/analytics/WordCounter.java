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
import edu.rit.asksg.service.ProviderService;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
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

    @Autowired
    ProviderService providerService;

    @Async
    public void work(final LocalDateTime day) {

        //for reuse
        final List<Topic> topics = topicRepository.findAll();
        final Map<String, Topic> topicMap = new HashMap<String, Topic>();
        for (Topic t : topics) {
            for (String s : t.getWords()) {
                topicMap.put(s, t);
            }
        }


        List<Service> services = providerService.findAllServices();

        for(Service s : services) {
            if(s.isEnabled()) {
                List<Conversation> conversations = conversationService.findByService(s, day, day.plusDays(1));
                Map<String, WordCount> countMap = buildCountMapWithService(conversations, s, topicMap, day);

                //Add zero values on this day
                List<WordCount> wordCounts = addZeroCounts(new ArrayList<WordCount>(countMap.values()), topics, s, day);

                logger.info("preparing to persist " + wordCounts.size() + " word counts for " + s.getName());

                try {
                    wordCountRepository.save(wordCounts);
                } catch (Error e) {
                    logger.error(e.getLocalizedMessage(), e);
                }


            }
        }
    }

    /**
     * If a topic is not found in word counts, add a zero value for that topic
     *  with given service and day
     * @param wordCounts
     * @param topics
     * @param s
     * @param day
     * @return
     */
    protected static List<WordCount> addZeroCounts(List<WordCount> wordCounts, Collection<Topic> topics, Service s, LocalDateTime day) {
        List<Topic> ts = new ArrayList<Topic>(topics);
        //remove topics that have positive counts
        for(WordCount wc : wordCounts) if(ts.contains(wc.getTopic())) ts.remove(wc.getTopic());
        //for all remaining (unseen) topics add zero word counts
        for(Topic t : ts) {
            WordCount wc = new WordCount();
            wc.setTopic(t);
            wc.setWordCount(0L);
            wc.setCreated(day);
            wc.setService(s);
            wordCounts.add(wc);
        }
        return wordCounts;
    }

    //todo: remove service and day because they can be figured out?
    /**
     * Looks at the conversations from a specific day & service and finds all words
     *  in the topic map
     * @param conversations
     * @param service
     * @param topicMap
     * @param day
     * @return
     */
    protected Map<String, WordCount> buildCountMapWithService(
            final List<Conversation> conversations,
            final Service service,
            final Map<String, Topic> topicMap,
            final LocalDateTime day) {

        Map<String, WordCount> countMap = new HashMap<String, WordCount>();
        for (Conversation c : conversations) {
            for (Message m : c.getMessages()) {
                for (String s : m.getContent().split("\\n| |\\r|;|\\(|\\)")) {
                    try {

                        //remove trailing punctuation , could be more here?
                        if (s.endsWith(".") || s.endsWith(",")) s = s.substring(0, s.length() - 1);
                        s = s.toLowerCase();

                        //Only count words we care about
                        if (topicMap.containsKey(s)) {
                            logger.debug("Found topic word: " + s);
                            if (countMap.containsKey(s)) {

                                WordCount wc = countMap.get(s);
                                wc.setWordCount(wc.getWordCount() + 1L);

                            } else {
                                WordCount wc = new WordCount();
                                wc.setTopic(topicMap.get(s));
                                wc.setWordCount(1L);
                                wc.setCreated(day);
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
        return countMap;
    }


}
