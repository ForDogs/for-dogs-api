package com.fordogs.core.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PortOneApiConstants {

    public static final String BASE_URI = "https://api.iamport.kr";
    public static final String ENDPOINT_GET_TOKEN = "/users/getToken";
    public static final String ENDPOINT_GET_PAYMENT = "/payments";
}
