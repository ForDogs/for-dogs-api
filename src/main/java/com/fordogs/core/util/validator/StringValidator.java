package com.fordogs.core.util.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringValidator {

    public static boolean validateLength(String value, int minLength, int maxLength) {
        return value.length() >= minLength && value.length() <= maxLength;
    }

    public static boolean validateBCryptEncoding(String value) {
        return Pattern.matches("^\\$2[ayb]\\$\\d\\d\\$.*", value);
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

    public static boolean validateKoreanEnglish(String value) {
        return Pattern.matches("^[a-zA-Zㄱ-ㅎ가-힣]+$", value);
    }

    public static boolean validateJWTToken(String value) {
        return Pattern.matches("^[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*$", value);
    }

    public static boolean validateNoConsecutiveChars(String value) {
        for (int i = 0; i < value.length() - 2; i++) {
            char first = value.charAt(i);
            char second = value.charAt(i + 1);
            char third = value.charAt(i + 2);

            if ((first + 1 == second && second + 1 == third) ||
                    (first - 1 == second && second - 1 == third)) {
                return false;
            }
        }
        return true;
    }
}
