package com.example.backend.service;

import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialShortDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SocialService {

    void createSocial(SocialLongDTO socialLongDTO);

    void deleteSocial(Long postId);

    void updateSocial(SocialLongDTO socialLongDTO);

    SocialLongDTO getSocial(Long socialId);

    List<SocialShortDTO> getSocialList();

    List<SocialShortDTO> getMySocialList();

    List<SocialShortDTO> getJoinSocialList();


}
