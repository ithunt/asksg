// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.Facebook;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Facebook_Roo_Json {
    
    public String Facebook.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static Facebook Facebook.fromJsonToFacebook(String json) {
        return new JSONDeserializer<Facebook>().use(null, Facebook.class).deserialize(json);
    }
    
    public static String Facebook.toJsonArray(Collection<Facebook> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<Facebook> Facebook.fromJsonArrayToFacebooks(String json) {
        return new JSONDeserializer<List<Facebook>>().use(null, ArrayList.class).use("values", Facebook.class).deserialize(json);
    }
    
}
