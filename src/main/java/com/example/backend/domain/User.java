package com.example.backend.domain;

import com.example.backend.domain.post.Feed;
import com.example.backend.domain.post.Social;
import com.example.backend.domain.enumType.UserType;
import lombok.*;

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
    @NotNull
    private String password;

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

    @OneToMany(mappedBy = "user")
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Social> socials = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Socialing> socialings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Like> likes = new ArrayList<>();

    private String refreshToken; //JWT

}
