package com.fordogs.core.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestConstants {

    public static final String REQUEST_ATTRIBUTE_USER_ID = "userId";
    public static final String REQUEST_ATTRIBUTE_SECURITY_AUTH_EXCEPTION = "SecurityAuthenticationException";
    public static final String COOKIE_NAME_REFRESH_TOKEN = "REFRESH_TOKEN";
}
