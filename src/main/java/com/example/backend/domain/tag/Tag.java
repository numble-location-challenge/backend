package com.example.backend.domain.tag;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Table(name = "TAG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    //SocialTag --> Tag 단방향 변경
//    @OneToMany(mappedBy = "social")
//    private List<SocialTag> socialTags = new ArrayList<>();

    @NotNull
    private String name;

    public Tag(Category category, @NotNull String name) {
        this.category = category;
        this.name = name;
    }
}
