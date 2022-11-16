package com.example.backend.service.social;

import com.example.backend.domain.post.Social;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialShortDTO;
import com.example.backend.repository.SocialRepository;
import com.example.backend.service.social.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
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
        socialRepository.deleteById(postId);
    }

    @Override
    public void updateSocial(SocialLongDTO socialLongDTO) {

    }

    @Override
    public SocialLongDTO getSocialDetail(Long socialId) {
        Social social = socialRepository.findById(socialId).orElseThrow();
        return null;
    }


    @Override
    public List<SocialShortDTO> getSocialList() {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        while(socialRepository.findAll().iterator().hasNext()){
            Social social = socialRepository.findAll().iterator().next();
            socialShortDTOList.add(new SocialShortDTO(social));
        }
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

    @Override
    public List<SocialShortDTO> filteringByCategory() {
        return null;
    }

    @Override
    public List<SocialShortDTO> filteringByTag() {
        return null;
    }

    @Override
    public List<SocialShortDTO> sortByLatest() {
        return null;
    }

    @Override
    public List<SocialShortDTO> sortByClosing() {
        return null;
    }

    @Override
    public List<SocialShortDTO> SortByPopularity() {
        return null;
    }
}
