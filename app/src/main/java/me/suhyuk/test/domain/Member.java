package me.suhyuk.test.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private int age;
    private String name;

    @Builder
    public Member(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
