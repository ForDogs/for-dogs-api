package com.fordogs.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConverterUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertArrayToJson(String[] array) {
        try {
            return objectMapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("배열을 JSON으로 변환하는 중 오류가 발생했습니다.", e);
        }
    }
}
