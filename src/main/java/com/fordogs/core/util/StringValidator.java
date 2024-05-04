package com.fordogs.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringValidator {

    public static boolean validateNonBlank(String value) {
        return !value.isBlank();
    }

    public static boolean validateMinimumLength(String value, int standard) {
        return value.length() >= standard;
    }

    public static boolean validateBase64Encoding(String value) {
        return Pattern.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$", value);
    }

    public static boolean validateEnglishNumber(String value) {
        return Pattern.matches("^[a-zA-Z0-9]+$", value);
    }

    public static boolean validateEmailDomainPattern(String value) {
        return Pattern.matches("^[0-9a-zA-Z.]+$", value);
    }

    public static boolean validatePasswordPattern(String value) {
        return Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d\\s]).*$", value);
    }

    public static boolean validateKoreanEnglishNumber(String value) {
        return Pattern.matches("^[a-zA-Zㄱ-ㅎ가-힣0-9]+$", value);
    }
}
