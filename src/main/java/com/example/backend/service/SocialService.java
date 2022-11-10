package com.example.backend.service;

import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.dto.SocialDTO;
import com.example.backend.dto.SocialingDTO;
import com.example.backend.repository.SocialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SocialService {
    @Autowired private SocialRepository socialRepo;

    //게시글 작성
    public SocialDTO createSocial(SocialDTO socialDTO){
        return socialRepo.save(socialDTO);
    }

    //게시글 출력
    public List<SocialDTO> getSocialList(){
        return (List<SocialDTO>) socialRepo.findAll();
    }

    //내가 작성한 모임 게시글 출력
    public Optional<SocialDTO> getMySocial(Long userId){
        return socialRepo.findAll().stream()
                .filter(SocialDTO -> Objects.equals(SocialDTO.getUser().getId(), userId))
                .findAny();
    }

    //내가 참여한 모임 출력
    public Optional<SocialDTO> getMyJoinSocial(Long userId){
        //사용자 id와 일치하는 socialing만 모은 리스트
        List<Socialing> socialings = socialRepo.findAll().stream().map(SocialDTO::getSocialings)
                .flatMap(List::stream)
                .filter(Socialing-> Objects.equals(Socialing.getUser().getId(),userId))
                .collect(Collectors.toList());

        return socialRepo.findAll().stream()
                .filter(SocialDTO -> SocialDTO.getSocialings().equals(socialings))
                .findAny();
    }

    //게시글 수정
    public SocialDTO updateSocial(SocialDTO socialDTO){
        Optional<SocialDTO> social = socialRepo.findById(socialDTO.getId());
        return socialRepo.save(socialDTO);

    }

    //게시글 삭제
    public void deleteSocial(SocialDTO socialDTO){
        socialRepo.deleteById(socialDTO.getId());
    }


}
