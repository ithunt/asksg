package edu.rit.asksg.analytics;

import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.dataio.AsyncWorker;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
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

    @Async
    public void work(final Service service) {
        LocalDateTime since = new LocalDateTime();
        since = since.minusWeeks(1);
        List<Conversation> conversations = conversationService.findByService(service, since);

        Map<String, WordCount> countMap = new HashMap<String, WordCount>();

        for(Conversation c : conversations) {


            for(Message m : c.getMessages()) {

                for(String s : m.getContent().split("\\n| |\\r|;|\\(|\\)")) {

                    try {

                        if(s.endsWith(".") || s.endsWith(",")) s=s.substring(0,s.length()-1);
                        s = s.toLowerCase();

                        if(countMap.containsKey(s)) {

                            WordCount wc = countMap.get(s);
                            wc.setWordCount(wc.getWordCount() + 1);

                        } else {

                            WordCount wc = new WordCount();
                            wc.setWord(s);
                            wc.setWordCount(1);
                            wc.setCreated(new LocalDateTime());
                            wc.setService(service.getName());
                            countMap.put(s, wc);
                        }
                    } catch (Error e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
                }
            }

        }


        try {
            wordCountRepository.save(countMap.values());
        } catch (Error e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }







}
