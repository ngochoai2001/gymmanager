package com.asignment.gymmanager.model;

public enum UserType {
    MEMBER("MEMBER"), VIP("VIP");
    private final String userType;
    UserType(String userType){
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}
