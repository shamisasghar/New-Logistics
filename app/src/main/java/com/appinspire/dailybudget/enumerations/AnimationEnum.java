package com.appinspire.dailybudget.enumerations;

/**
 * Created by BILAL on 10/8/2017.
 */
public enum AnimationEnum {
    HORIZONTAL(0),
    VERTICAL(1);

    private final int id;

    AnimationEnum(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
