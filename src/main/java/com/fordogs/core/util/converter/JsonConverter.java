package com.fordogs.core.util.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertArrayToJson(String[] array) {
        try {
            return objectMapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("배열을 JSON으로 변환하는 중 오류가 발생했습니다.", e);
        }
    }

    public static String[] convertJsonToArray(String json) {
        try {
            return objectMapper.readValue(json, String[].class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON을 배열로 변환하는 중 오류가 발생했습니다.", e);
        }
    }
}
