package com.example.backend.domain;

import com.example.backend.domain.post.Feed;
import com.example.backend.domain.post.Social;
import com.example.backend.domain.enumType.UserType;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "USERS", uniqueConstraints =
        {@UniqueConstraint(name = "EMAIL_NICKNAME_UNIQUE", columnNames = {"email","nickname"})})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private UserType userType; // 기본 회원, 카카오 회원 구분

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
    private String phoneNumber;
    @NotNull
    private int region;
    private String profile;
    private String bio;//한마디 소개글

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Socialing> socialings = new ArrayList<>();

    @Column(length = 500)
    private String refreshToken; //JWT

//    @OneToMany(mappedBy = "user")
//    private List<Feed> feeds = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private List<Social> socials = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private List<Comment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Like> likes = new ArrayList<>();


    public void encodePassword(PasswordEncoder passwordEncoder){
        password = passwordEncoder.encode(password);
    }

    public void setId(Long id){
        this.id = id;
    }

    @Builder
    public User(@NotNull UserType userType, @NotNull String email, @NotNull String password, @NotNull String username, @NotNull String nickname, @NotNull String phoneNumber, @NotNull int region, String bio) {
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.region = region;
        this.bio = bio;
    }

    //==연관관계 메서드==//
    public void addSocialing(Socialing socialing){
        socialings.add(socialing);
        socialing.setUser(this);
    }

    public void deleteSocialing(Socialing socialing){
        socialings.remove(socialing);
    }


    //==수정 메서드==//

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

}
