package edu.rit.asksg.analytics.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.List;

@RooJavaBean
@RooToString
@RooJson(deepSerialize = true)
public class GraphData {

    public String topic;

    //Invariant, length of these arrays are the same

    public List<Long> dates;
    public List<Integer> wordCounts;
    public List<Double> sentiments;

}
