package com.rest.hgq.core;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static <T> T fromJson(String json, Class<T> clz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        try {
            return objectMapper.readValue(json, clz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize json: " + e.getMessage(), e);
        }
    }

    public static <T> List<T> jsonToList(String json, Class<T> clz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        try {
            JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clz);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize json to List: " + e.getMessage(), e);
        }
    }

    public static String toJson(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode toJsonNode(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
