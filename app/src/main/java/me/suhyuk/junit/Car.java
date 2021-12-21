package me.suhyuk.junit;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Car {
    private int speed;
    @Builder
    public Car(int speed) {
        this.speed = speed;
    }
}

