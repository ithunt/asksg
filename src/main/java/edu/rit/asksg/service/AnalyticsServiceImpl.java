package edu.rit.asksg.service;

import edu.rit.asksg.analytics.domain.GraphData;
import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import edu.rit.asksg.specification.CreatedSinceSpecification;
import edu.rit.asksg.specification.CreatedUntilSpecification;
import edu.rit.asksg.specification.EqualSpecification;
import edu.rit.asksg.specification.Specification;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Specification<WordCount> spec = new EqualSpecification<WordCount>("topic", topic);
        spec = spec.and(new CreatedSinceSpecification<WordCount>(since));
        spec = spec.and(new CreatedUntilSpecification<WordCount>(until));

        return wordCountRepository.findAll(spec);
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

            int total = 0;
            for(WordCount wc : findWordCountsWith(t, since3, since2)) {
                total += wc.getWordCount();
            }
            dates.add(since3.toDate().getTime());
            counts.add(total);


            total = 0;
            for(WordCount wc : findWordCountsWith(t, since2, since1)) {
                total += wc.getWordCount();
            }
            dates.add(since2.toDate().getTime());
            counts.add(total);


            total = 0;
            for(WordCount wc : findWordCountsWith(t, since1, LocalDateTime.now())) {
                total += wc.getWordCount();
            }
            dates.add(since1.toDate().getTime());
            counts.add(total);

            gd.setDates(dates);
            gd.setWordCounts(counts);
            gd.setSentiments(randomSentiments(gd.getWordCounts().size()));

            graphData.add(gd);

        }

        return graphData;
    }

    protected List<Double> randomSentiments(int count) {
        List<Double> l = new ArrayList<Double>();

        for(int i=0;i<count;i++) {
            l.add((Math.random() * 2) - 1);
        }
        return l;
    }
}
