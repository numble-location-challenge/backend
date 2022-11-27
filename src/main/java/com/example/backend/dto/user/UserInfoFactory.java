package com.example.backend.dto.user;

import com.example.backend.domain.enumType.UserType;

/**
 * UserType에 따라 알맞은 구현체를 생성
 */
public class UserInfoFactory {
    public static UserInfo getOAuth2UserInfo(UserType userType) {
        switch (userType) {
            case KAKAO: return new KakaoUserDTO();
            default: throw new IllegalArgumentException("Invalid User Type.");
        }
    }

    public static SnsUserDTO getUserDTOFromUserInfo(UserType userType, UserInfo userInfo){
        switch (userType){
            case KAKAO: return getKakaoDTO(((KakaoUserDTO) userInfo));
            default: throw new IllegalArgumentException("Invalid User Type.");
        }
    }

    private static SnsUserDTO getKakaoDTO(KakaoUserDTO userInfo){
        return SnsUserDTO.builder()
                .id(userInfo.getId())
                .email(userInfo.getKakao_account().getEmail())
                .nickname(userInfo.getProperties().getNickname())
                .profile(userInfo.getProperties().getProfile_image())
                .build();
    }
}

