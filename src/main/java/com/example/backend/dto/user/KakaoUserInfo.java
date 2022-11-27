package com.example.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo extends UserInfo{
    private Long id;//id, 사용자의 카카오 회원번호
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Properties {
        private String nickname;
        public String profile_image;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount{
        private Boolean has_email;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
    }
}
