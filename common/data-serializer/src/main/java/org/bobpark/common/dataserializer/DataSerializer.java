package org.bobpark.common.dataserializer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSerializer {

    private static final ObjectMapper om = initialize();

    private static ObjectMapper initialize() {
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T deserialize(String data, Class<T> clazz) {
        try {
            return om.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            log.error("[DataSerializer.deserialize] data=[{}], clazz=[{}]", data, clazz, e);
            return null;
        }
    }

    public static <T> T deserialize(Object data, Class<T> clazz) {
        return om.convertValue(data, clazz);
    }

    public static String serialize(Object data) {
        try {
            return om.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("[DataSerializer.serialize] data=[{}]", data, e);
            return null;
        }
    }

}
