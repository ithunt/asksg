package edu.rit.asksg.service;

import edu.rit.asksg.analytics.domain.GraphData;
import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {


    @Autowired
    TopicRepository topicRepository;

    @Autowired
    WordCountRepository wordCountRepository;

    @Override
    public List<WordCount> findWordCountInRange(final Topic topic, final LocalDateTime since, final LocalDateTime until) {
        Specification<WordCount> specification = new Specification<WordCount>() {
            @Override
            public Predicate toPredicate(Root<WordCount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<LocalDateTime> created = root.get("created");

                Predicate[] predicates = new Predicate[3];
                predicates[0] = root.get("word").in(topic.getWords());
                predicates[1] = cb.greaterThan(created, since);
                predicates[2] = cb.lessThan(created, until);

                return cb.and(predicates);
            }
        };

        return wordCountRepository.findAll(specification);
    }



    public List<GraphData> getAggregatedWordCount() {
        final List<Topic> topics = topicRepository.findAll();

        LocalDateTime since1 = new LocalDateTime();
        LocalDateTime since2 = new LocalDateTime();
        LocalDateTime since3 = new LocalDateTime();

        since1 = since1.minusDays(3);
        since2 = since2.minusDays(6);
        since3 = since3.minusDays(9);

        List<GraphData> graphData = new ArrayList<GraphData>();

        for(final Topic t : topics) {
            GraphData gd = new GraphData();
            gd.setTopic(t.getName());

            List<Long> dates = new ArrayList<Long>();
            List<Integer> counts = new ArrayList<Integer>();

            int total = 0;
            for(WordCount wc : findWordCountInRange(t, since3, since2)) {
                total += wc.getWordCount();
            }
            dates.add(since3.toDate().getTime());
            counts.add(total);


            total = 0;
            for(WordCount wc : findWordCountInRange(t, since2, since1)) {
                total += wc.getWordCount();
            }
            dates.add(since2.toDate().getTime());
            counts.add(total);


            total = 0;
            for(WordCount wc : findWordCountInRange(t, since1, LocalDateTime.now())) {
                total += wc.getWordCount();
            }
            dates.add(since1.toDate().getTime());
            counts.add(total);

            gd.setDates(dates);
            gd.setWordCounts(counts);
            gd.setSentiments(new ArrayList<Double>());

            graphData.add(gd);

        }

        return graphData;
    }
}
