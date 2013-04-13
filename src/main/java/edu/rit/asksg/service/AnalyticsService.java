package edu.rit.asksg.service;

import edu.rit.asksg.analytics.domain.Topic;

import java.util.Map;

public interface AnalyticsService {

    Map<Topic, Integer> getAggregatedWordCount();

}
