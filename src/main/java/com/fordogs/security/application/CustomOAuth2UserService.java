package com.fordogs.security.application;

import com.fordogs.security.application.dto.CustomOAuth2User;
import com.fordogs.security.application.dto.OAuth2UserInfo;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        UserEntity userEntity = getOrSave(oAuth2UserInfo);

        return CustomOAuth2User.builder()
                .userEntity(userEntity)
                .attributes(oAuth2UserAttributes)
                .attributeKey(userNameAttributeName)
                .build();
    }

    private UserEntity getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        UserEntity member = userRepository.findByEmailAndAccount(oAuth2UserInfo.email(), oAuth2UserInfo.account())
                .orElseGet(oAuth2UserInfo::toEntity);
        return userRepository.save(member);
    }
}
