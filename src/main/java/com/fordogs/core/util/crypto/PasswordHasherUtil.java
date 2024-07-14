package com.fordogs.core.util.crypto;

import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordHasherUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    public static String encode(String rawPassword) {
        try {
            return PasswordHasherUtil.passwordEncoder.encode(rawPassword);
        } catch (Exception e) {
            throw GlobalErrorCode.internalServerException("비밀번호를 인코딩하는 중 예외가 발생했습니다.");
        }
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return PasswordHasherUtil.passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            throw GlobalErrorCode.internalServerException("비밀번호를 비교하는 중 예외가 발생했습니다.");
        }
    }
}
