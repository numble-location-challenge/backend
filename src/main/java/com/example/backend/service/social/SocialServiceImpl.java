package com.example.backend.service.social;

import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialShortDTO;
import com.example.backend.repository.SocialRepository;
import com.example.backend.service.social.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialServiceImpl implements SocialService {

    private final SocialRepository socialRepository;

    @Override
    public void createSocial(SocialLongDTO socialLongDTO) {

    }

    @Override
    public void deleteSocial(Long postId) {

    }

    @Override
    public void updateSocial(SocialLongDTO socialLongDTO) {

    }

    @Override
    public SocialLongDTO getSocialDetail(Long socialId) {
        return null;
    }

    @Override
    public List<SocialShortDTO> getSocialList() {
        return null;
    }

    @Override
    public List<SocialShortDTO> getMySocialList() {
        return null;
    }

    @Override
    public List<SocialShortDTO> getJoinSocialList() {
        return null;
    }
}
