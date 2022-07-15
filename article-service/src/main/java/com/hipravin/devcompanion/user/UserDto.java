package com.hipravin.devcompanion.user;

public class UserDto {
    private String name;

    public UserDto() {
    }

    public UserDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
