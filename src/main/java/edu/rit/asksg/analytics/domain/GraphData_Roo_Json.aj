// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.analytics.domain;

import edu.rit.asksg.analytics.domain.GraphData;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect GraphData_Roo_Json {

    public String GraphData.toJson() {
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
    }

    public static GraphData GraphData.fromJsonToGraphData(String json) {
        return new JSONDeserializer<GraphData>().use(null, GraphData.class).deserialize(json);
    }

    public static String GraphData.toJsonArray(Collection<GraphData> collection) {
        return new JSONSerializer().exclude("*.class").deepSerialize(collection);
    }

    public static Collection<GraphData> GraphData.fromJsonArrayToGraphDatas(String json) {
        return new JSONDeserializer<List<GraphData>>().use(null, ArrayList.class).use("values", GraphData.class).deserialize(json);
    }

}
