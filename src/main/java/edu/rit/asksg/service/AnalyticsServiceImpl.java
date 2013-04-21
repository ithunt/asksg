package edu.rit.asksg.service;

import edu.rit.asksg.analytics.domain.GraphData;
import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import org.joda.time.DateMidnight;
import org.joda.time.Days;
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
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {


    @Autowired
    TopicRepository topicRepository;

    @Autowired
    WordCountRepository wordCountRepository;

    @Override
    public List<WordCount> findWordCountsWith(final Topic topic, final LocalDateTime since, final LocalDateTime until) {

        Specification<WordCount> specification = new Specification<WordCount>() {
            @Override
            public Predicate toPredicate(Root<WordCount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Path<LocalDateTime> created = root.get("created");
                Predicate[] predicates = new Predicate[3];
                predicates[0] = cb.equal(root.get("topic"), topic);
                predicates[1] = cb.greaterThan(created, since);
                predicates[2] = cb.lessThan(created, until);

                return cb.and(predicates);
            }
        };

        return wordCountRepository.findAll(specification);
    }


    public int getTotalWordCount(Topic t, LocalDateTime since, LocalDateTime until) {
        if(t == null || since == null || until == null) return 0;

        int total = 0;
        for(WordCount wc : findWordCountsWith(t, since, until)) {
            total += wc.getWordCount();
        }

        return total;
    }


    public List<GraphData> getGraphDataInRange(LocalDateTime start, LocalDateTime end) {
        final int days = Days.daysBetween( new DateMidnight(start.toDateTime()), new DateMidnight(end.toDateTime())).getDays();
        final DateMidnight s = new DateMidnight(start.toDateTime());

        List<GraphData> data = new ArrayList<GraphData>();

        for(Topic t : topicRepository.findAll()) {
            GraphData gd = new GraphData();
            gd.setTopic(t.getName());

            List<Long> dates = new ArrayList<Long>();
            List<Integer> counts = new ArrayList<Integer>();

            for(int i=0;i<=days;i++) {
                DateMidnight since = s.plusDays(i);
                DateMidnight until = s.plusDays(i+1);

                dates.add(since.toDate().getTime());
                counts.add(getTotalWordCount(t, new LocalDateTime(since), new LocalDateTime(until)));
            }

            gd.setDates(dates);
            gd.setWordCounts(counts);
            gd.setSentiments(randomSentiments(gd.getWordCounts().size()));

            data.add(gd);
        }

        return data;
    }


    public List<GraphData> getAggregatedWordCount() {
        final List<Topic> topics = topicRepository.findAll();

        LocalDateTime since1 = new LocalDateTime();
        LocalDateTime since2 = new LocalDateTime();
        LocalDateTime since3 = new LocalDateTime();

        since1 = since1.minusDays(1);
        since2 = since2.minusDays(2);
        since3 = since3.minusDays(3);

        List<GraphData> graphData = new ArrayList<GraphData>();

        for(final Topic t : topics) {
            GraphData gd = new GraphData();
            gd.setTopic(t.getName());

            List<Long> dates = new ArrayList<Long>();
            List<Integer> counts = new ArrayList<Integer>();

            dates.add(since3.toDate().getTime());
            counts.add(getTotalWordCount(t, since3, since2));

            dates.add(since2.toDate().getTime());
            counts.add(getTotalWordCount(t, since2, since1));

            dates.add(since1.toDate().getTime());
            counts.add(getTotalWordCount(t, since1, LocalDateTime.now()));

            gd.setDates(dates);
            gd.setWordCounts(counts);
            gd.setSentiments(randomSentiments(gd.getWordCounts().size()));

            graphData.add(gd);

        }

        return graphData;
    }

    protected static List<Double> randomSentiments(int count) {
        List<Double> l = new ArrayList<Double>();

        for(int i=0;i<count;i++) {
            l.add((Math.random() * 2) - 1);
        }
        return l;
    }
}
