package com.example.backend.domain.tag;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Table(name = "CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @NotNull
    private String name;

//    @OneToMany(mappedBy = "category")
//    private List<Social> socials = new ArrayList<>();

    public Category(@NotNull String name) {
        this.name = name;
    }
}
