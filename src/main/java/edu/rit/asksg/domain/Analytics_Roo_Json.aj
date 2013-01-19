// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Analytics_Roo_Json {

    public String Analytics.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static Analytics Analytics.fromJsonToAnalytics(String json) {
        return new JSONDeserializer<Analytics>().use(null, Analytics.class).deserialize(json);
    }

    public static String Analytics.toJsonArray(Collection<Analytics> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<Analytics> Analytics.fromJsonArrayToAnalyticses(String json) {
        return new JSONDeserializer<List<Analytics>>().use(null, ArrayList.class).use("values", Analytics.class).deserialize(json);
    }

}
