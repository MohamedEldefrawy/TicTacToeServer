package model.Utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class JsonBuilder {

    public static <T> String serializeObject(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T deSerializeObject(String json) {
        Type typeOfT = new TypeToken<T>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }
}
