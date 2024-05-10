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
        return PasswordUtil.passwordEncoder.encode(rawPassword);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PasswordUtil.passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
