package me.suhyuk.junit;

import lombok.*;

@Getter
@Setter(AccessLevel.PROTECTED)
@ToString
public class Maker {

    private String name;
    private Integer age;

    @Builder
    public Maker(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s,%d", name, age);
    }

}
