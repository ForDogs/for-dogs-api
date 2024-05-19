package com.fordogs.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    public static String encode(String rawPassword) {
        try {
            return PasswordUtil.passwordEncoder.encode(rawPassword);
        } catch (Exception e) {
            throw new IllegalStateException("비밀번호를 인코딩하는 중 예외가 발생했습니다.", e);
        }
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return PasswordUtil.passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            throw new IllegalStateException("비밀번호를 비교하는 중 예외가 발생했습니다.", e);
        }
    }
}
