package com.example.backend.service.user;

import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.user.SnsUserDTO;


public interface SnsAPIService {

    Long getSnsId(UserType userType, String accessToken);

    SnsUserDTO getUserInfo(UserType userType, String accessToken);

    Long unlink(UserType userType, String accessToken);

    //Test
//    TestKakaoToken requestToken(String code) throws IOException;
}
