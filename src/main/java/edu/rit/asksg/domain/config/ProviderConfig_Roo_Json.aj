// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain.config;

import edu.rit.asksg.domain.config.ProviderConfig;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ProviderConfig_Roo_Json {
    
    public String ProviderConfig.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static ProviderConfig ProviderConfig.fromJsonToProviderConfig(String json) {
        return new JSONDeserializer<ProviderConfig>().use(null, ProviderConfig.class).deserialize(json);
    }
    
    public static String ProviderConfig.toJsonArray(Collection<ProviderConfig> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<ProviderConfig> ProviderConfig.fromJsonArrayToProviderConfigs(String json) {
        return new JSONDeserializer<List<ProviderConfig>>().use(null, ArrayList.class).use("values", ProviderConfig.class).deserialize(json);
    }
    
}
