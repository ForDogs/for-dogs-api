package com.fordogs.core.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenConstants {

    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    public static final String USER_ID_CLAIM = "id";

    public static final String ROLE_CLAIM = "role";

    public static final String UUID_TOKEN_CLAIM = "uuid";
}
