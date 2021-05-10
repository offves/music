package com.offves.music.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper COMMON_OBJECT_MAPPER;

    private static final ObjectMapper PRETTY_OBJECT_MAPPER;

    static {
        COMMON_OBJECT_MAPPER = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PRETTY_OBJECT_MAPPER = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleModule dateFormatModule = new SimpleModule()
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));

        PRETTY_OBJECT_MAPPER.registerModule(dateFormatModule);
        COMMON_OBJECT_MAPPER.registerModule(dateFormatModule);
    }

    public static <T> T parse(String jsonString, Class<T> clazz) {
        try {
            return COMMON_OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T parse(String jsonString, TypeReference<T> typeReference) {
        try {
            return COMMON_OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        try {
            JavaType javaType = COMMON_OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return COMMON_OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getValue(String jsonString, String key) {
        try {
            ObjectNode jsonNodes = COMMON_OBJECT_MAPPER.readValue(jsonString, ObjectNode.class);
            JsonNode jsonNode = jsonNodes.get(key);
            return jsonNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toString(Object object) {
        try {
            return COMMON_OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toPrettyString(Object object) {
        try {
            return PRETTY_OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static ObjectMapper getCommonObjectMapper() {
        return COMMON_OBJECT_MAPPER;
    }

}
