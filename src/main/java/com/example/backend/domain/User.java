package com.example.backend.domain;

import com.example.backend.domain.enumType.UserStatus;
import com.example.backend.domain.enumType.UserType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Table(name = "USERS", uniqueConstraints =
        {@UniqueConstraint(name = "EMAIL_NICKNAME_UNIQUE", columnNames = {"email","nickname", "sns_id"})})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType; // 기본 회원, 카카오 회원 구분

    @Column(name = "sns_id")
    private Long snsId; //sns 계정 회원가입 유무 확인용

    @NotNull
    private String email; // 아이디

    private String password; //카카오 유저는 비밀번호가 없음

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;
    @NotNull
    @Column(name = "dong_code")
    private Long dongCode;
    @NotNull
    @Column(name = "dong_name")
    private String dongName;

    private String profile;
    private String bio;//한마디 소개글

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Socialing> socialings = new ArrayList<>();

    @Column(length = 1000, name = "refresh_token")
    private String refreshToken; //JWT

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    public void encodePassword(PasswordEncoder passwordEncoder){
        password = passwordEncoder.encode(password);
    }

    public void setDefaultUser(){
        userType = UserType.DEFAULT;
        userStatus = UserStatus.ACTIVATED;
    }

    public void setKakaoUser(Long snsId){
        this.snsId = snsId;
        userType = UserType.KAKAO;
        userStatus = UserStatus.ACTIVATED;
    }

    public void setWithdrawStatus(){
        userStatus = UserStatus.WITHDRAW;
    }

    public Integer getRegionCodeFromDongCode(){
        String s = dongCode.toString();
        Integer regionCode = Integer.parseInt(s.substring(0, 4));//앞 5자리가 시군구 코드
        log.info("dongCode={}, regionCode={}",dongCode, regionCode);
        return regionCode;
    }

    @Builder
    public User(@NotNull UserType userType, @NotNull String email, @NotNull String password, @NotNull String username, @NotNull String nickname, @NotNull String phoneNumber, @NotNull Long dongCode, @NotNull String dongName, @NotNull UserStatus userStatus, String bio) {
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.dongCode = dongCode;
        this.dongName = dongName;
        this.userStatus = userStatus;
        this.bio = bio;
    }

    //==수정 메서드==//
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateBio(String bio){
        this.bio = bio;
    }

    public void updateProfile(String profile){
        this.profile = profile;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateRegion(Long dongCode, String dongName){
        this.dongCode = dongCode;
        this.dongName = dongName;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

}
