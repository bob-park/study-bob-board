package org.bobpark.common.dataserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSerializer {

    private static final ObjectMapper om = initialize();

    private static ObjectMapper initialize() {

        // Java time module
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addDeserializer(
            LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));

        Jackson2ObjectMapperBuilder builder =
            new Jackson2ObjectMapperBuilder() {
                @Override
                public void configure(@NonNull ObjectMapper objectMapper) {
                    super.configure(objectMapper);
                    objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
                    objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
                    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                }
            };
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.modulesToInstall(jtm);

        return builder.build();
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
