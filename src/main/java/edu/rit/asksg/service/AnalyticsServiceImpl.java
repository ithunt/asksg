package edu.rit.asksg.service;

import com.google.common.base.Joiner;
import edu.rit.asksg.analytics.domain.GraphData;
import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.repository.TopicRepository;
import edu.rit.asksg.repository.WordCountRepository;
import edu.rit.asksg.specification.CreatedSinceSpecification;
import edu.rit.asksg.specification.CreatedUntilSpecification;
import edu.rit.asksg.specification.EqualSpecification;
import edu.rit.asksg.specification.Specification;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
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

    @Override
    public String buildCSV(final LocalDateTime since, final LocalDateTime until) {
        List<String> csv = new ArrayList<String>();
        csv.add("word,service,count,date,sentiment,synonyms");
        for(Topic t : topicRepository.findAll()) {
            for(WordCount wc : findWordCountsWith(t,since,until)) {
                csv.add(t.getName() + "," +
                        wc.getService().getName() + "," +
                        wc.getWordCount() + "," +
                        wc.getCreated() + "," +
                        ((Math.random() * 2) - 1) + "," +
                        "[" + Joiner.on(",").join(t.getWords()) + "]"

                        );
            }

        }
        return Joiner.on("\n").join(csv);
    }

    public long getTotalWordCount(Topic t, LocalDateTime since, LocalDateTime until) {
        if(t == null || since == null || until == null) return 0;

        long total = 0;
        for(WordCount wc : findWordCountsWith(t, since, until)) {
            total += wc.getWordCount();
        }

        return total;
    }


    public static List<DateTime> getDaySpan(LocalDateTime start, LocalDateTime end) {
        final int days = Days.daysBetween( new DateMidnight(start.toDateTime()), new DateMidnight(end.toDateTime())).getDays();
        final DateMidnight s = new DateMidnight(start.toDateTime());

        List<DateTime> span = new ArrayList<DateTime>();

        for(int i=0;i<=days;i++) {
            span.add(s.plusDays(i).toDateTime());
        }

        return span;
    }


    public List<GraphData> getGraphDataInRange(LocalDateTime start, LocalDateTime end) {
        List<GraphData> data = new ArrayList<GraphData>();

        for(Topic t : topicRepository.findAll()) {
            GraphData gd = new GraphData();
            gd.setTopic(t.getName());

            List<Long> dates = new ArrayList<Long>();
            List<Long> counts = new ArrayList<Long>();

            for(DateTime d : getDaySpan(start, end)) {
                dates.add(d.toDate().getTime());
                counts.add(getTotalWordCount(t, new LocalDateTime(d), new LocalDateTime(d.plusDays(1))));
            }

            gd.setDates(dates);
            gd.setWordCounts(counts);
            gd.setSentiments(randomSentiments(gd.getWordCounts().size()));

            data.add(gd);
        }

        return data;
    }

    /**
     * Use above method
     * @return
     */
    @Deprecated
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
            List<Long> counts = new ArrayList<Long>();

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
