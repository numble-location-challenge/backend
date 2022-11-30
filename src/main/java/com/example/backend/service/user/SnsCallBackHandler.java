package com.example.backend.service.user;

import com.example.backend.domain.User;
import com.example.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SnsCallBackHandler {

    private final UserService userService;

    @Value("${sns.kakao.adminKey}")
    private String KAKAO_ADMIN_KEY;

    @Value("${sns.kakao.appId}")
    private Long KAKAO_APP_ID;

    //카카오 연결끊기 알림 콜백 메서드
    @GetMapping("/users/disconnect/kakao")
    public ResponseEntity<?> disConnectFromKakao(
            @RequestParam(value = "app_id") Long appId,
            @RequestParam(value = "user_id") Long snsId,
            @RequestParam(value = "referrer_type") String referrerType,
            @RequestHeader(value = "Authorization") String appAdminKey){
        //요청 유효성 검증
        if(appAdminKey.equals(KAKAO_ADMIN_KEY) && appId.equals(KAKAO_APP_ID)){
            //사용자 정보 삭제
            User findUser = userService.getUserBySnsId(snsId); //사용자 못찾아도 예외처리X
            if(findUser != null) userService.changeToWithdrawnUser(findUser);

            //이미 서비스에서 탈퇴 처리된 사용자의 연결 끊기 알림이 온 경우에도 성공 응답
            return ResponseEntity.ok().build(); //3초이내로 응답해야함
        }

        return ResponseEntity.badRequest().build();
    }
}
