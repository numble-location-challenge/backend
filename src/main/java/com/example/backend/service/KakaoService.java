package com.example.backend.service;

import com.example.backend.dto.user.KakaoUserDTO;

public interface KakaoService {

    Long getUserId(String accessToken);

    KakaoUserDTO getUserInfo(String accessToken);

}
