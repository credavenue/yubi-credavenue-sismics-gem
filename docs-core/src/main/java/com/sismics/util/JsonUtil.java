package com.sismics.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.StringReader;

/**
 * JSON utilities.
 * 
 * @author bgamard
 */
public class JsonUtil {
    /**
     * Returns a JsonValue from a String.
     * 
     * @param value Value
     * @return JsonValue
     */
    public static JsonValue nullable(String value) {
        if (value == null) {
            return JsonValue.NULL;
        }
        return Json.createObjectBuilder().add("_", value).build().get("_");
    }
    
    /**
     * Returns a JsonValue from an Integer.
     * 
     * @param value Value
     * @return JsonValue
     */
    public static JsonValue nullable(Integer value) {
        if (value == null) {
            return JsonValue.NULL;
        }
        return Json.createObjectBuilder().add("_", value).build().get("_");
    }

    /**
     * Returns a JsonValue from an Long.
     *
     * @param value Value
     * @return JsonValue
     */
    public static JsonValue nullable(Long value) {
        if (value == null) {
            return JsonValue.NULL;
        }
        return Json.createObjectBuilder().add("_", value).build().get("_");
    }

    /**
     * Returns a JsonValue from JsonObject.
     *
     * @param value Value
     * @return JsonValue
     */
    public static JsonValue nullable(JsonObject value) {
        if (value == null) {
            return JsonValue.NULL;
        }
        return Json.createObjectBuilder().add("_", value).build().get("_");
    }

    public static JsonObject convertRawStringJson(String rawString) {
        if (rawString != null) {
            try {
                JsonReader reader = Json.createReader(new StringReader(rawString));
                return reader.readObject();
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }
}
