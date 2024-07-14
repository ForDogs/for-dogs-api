package com.fordogs.core.util.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static String convertArrayToJson(String[] array) {
        try {
            return objectMapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            throw GlobalErrorCode.internalServerException("배열을 JSON으로 변환하는 중 오류가 발생했습니다.");
        }
    }

    public static String[] convertJsonToArray(String json) {
        try {
            return objectMapper.readValue(json, String[].class);
        } catch (JsonProcessingException e) {
            throw GlobalErrorCode.internalServerException("JSON을 배열로 변환하는 중 오류가 발생했습니다.");
        }
    }

    public static String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw GlobalErrorCode.internalServerException("객체를 JSON으로 변환하는 중 오류가 발생했습니다.");
        }
    }
}
