package com.example;

public class User {
    private String username;
    private String password;
    private String id;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getId(){
        return this.id;
    }

    public void setUsername(String n) {
        this.username = n;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public void setId(String i){
        this.id = i;
    }

}