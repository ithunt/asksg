package edu.rit.asksg.service;

import edu.rit.asksg.analytics.domain.GraphData;
import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.analytics.domain.WordCount;
import org.joda.time.LocalDateTime;

import java.util.List;

public interface AnalyticsService {

    List<GraphData> getAggregatedWordCount();

    List<WordCount> findWordCountsWith(Topic topic, LocalDateTime since, LocalDateTime until);

}
