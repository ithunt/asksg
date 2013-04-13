package edu.rit.asksg.service;

import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {


    @Autowired
    TopicRepository topicRepository;

    @Autowired
    WordCountRepository wordCountRepository;


    public Map<Topic, Integer> getAggregatedWordCount() {

        final List<Topic> topics = topicRepository.findAll();
        final Map<Topic, Integer> aggregated = new HashMap<Topic, Integer>();

        for(final Topic t : topics) {
            Specification<WordCount> specification = new Specification<WordCount>() {
                @Override
                public Predicate toPredicate(Root<WordCount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return root.get("word").in(t.getWords());
                }
            };

            int total = 0;
            for(WordCount wc :  wordCountRepository.findAll(specification)) {
                total += wc.getWordCount();
            }
            aggregated.put(t, total);
        }

        return aggregated;
    }
}
