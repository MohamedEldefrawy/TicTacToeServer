package model.Utilities;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class JsonBuilder {

    public static <T> String serializeObject(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);

    }

    public static <T> T deSerializeObject(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

}
