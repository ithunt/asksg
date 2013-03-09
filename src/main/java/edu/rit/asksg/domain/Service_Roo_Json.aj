// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.Service;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Service_Roo_Json {

	public String Service.toJson() {
		return new JSONSerializer().exclude("*.class").deepSerialize(this);
	}

	public static Service Service.fromJsonToService(String json) {
		return new JSONDeserializer<Service>().use(null, Service.class).deserialize(json);
	}

	public static String Service.toJsonArray(Collection<Service> collection) {
		return new JSONSerializer().exclude("*.class").deepSerialize(collection);
	}

	public static Collection<Service> Service.fromJsonArrayToServices(String json) {
		return new JSONDeserializer<List<Service>>().use(null, ArrayList.class).use("values", Service.class).deserialize(json);
	}

}
