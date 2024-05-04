package com.fordogs.user.application;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.user.error.UserErrorCode;
import com.fordogs.user.presentation.dto.JoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public JoinDto.Response joinUser(JoinDto.Request request) {
        UserEntity requestUserEntity = request.toEntity();

        if (userRepository.existsByUserId(requestUserEntity.getUserId())) {
            throw UserErrorCode.DUPLICATE_USER_ID.toException();
        }

        UserEntity saveUserEntity = userRepository.save(requestUserEntity);

        return JoinDto.Response.builder()
                .userId(saveUserEntity.getUserId().getValue())
                .userName(saveUserEntity.getName().getValue())
                .build();
    }
}
