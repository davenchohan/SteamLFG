package com.example;

public class User {
    private String username;
    private String password;
    private int id;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getId(){
        return this.id;
    }

    public void setUsername(String n) {
        this.username = n;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public void setId(int i){
        this.id = i;
    }

}