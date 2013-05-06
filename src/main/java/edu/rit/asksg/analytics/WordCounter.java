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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Async
    public void work(final LocalDateTime day) {

        //for reuse
        final List<Topic> topics = topicRepository.findAll();

        List<Service> services = providerService.findAllServices();

        for(Service s : services) {
            if(s.isEnabled()) {
                List<Conversation> conversations = conversationService.findByService(s, day, day.plusDays(1));
                Map<Topic, WordCount> countMap = buildCountMapWithService(conversations, s, topics, day);

                //Add zero values on this day
                List<WordCount> wordCounts = addZeroCounts(new ArrayList<WordCount>(countMap.values()), topics, s, day);

//                logger.info("preparing to persist " + wordCounts.size() + " word counts for " + s.getName());

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
     * @param topics
     * @param day
     * @return
     */
    protected Map<Topic, WordCount> buildCountMapWithService(
            final List<Conversation> conversations,
            final Service service,
            final List<Topic> topics,
            final LocalDateTime day) {

        Map<Topic, WordCount> countMap = new HashMap<Topic, WordCount>();

        for (Conversation c : conversations) {
            for (Message m : c.getMessages()) {
                String content = m.getContent();
                if(content != null) {
                    for(Topic t : topics) {
                        for(String word : t.getWords()) {
                            if(content.contains(word)) {
                                if (countMap.containsKey(t)) {

                                    WordCount wc = countMap.get(t);
                                    wc.setWordCount(wc.getWordCount() + 1L);

                                } else {
                                    WordCount wc = new WordCount();
                                    wc.setTopic(t);
                                    wc.setWordCount(1L);
                                    wc.setCreated(day);
                                    wc.setService(service);
                                    countMap.put(t, wc);
                                }
                            }
                        } //end for all words
                    } // end for all topics
                }
            } //end for all messages in a convo
        } //end for all convos
        return countMap;
    }


}
