package com.example;

public class User {
    private String username;
    private String password;
    private int age;
    private String sex;
    private String region;
    private String bio;
    private String pfp;
    private String groups;
    private int id;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getId() {
        return this.id;
    }

    public int getAge() {
        return this.age;
    }

    public String getSex() {
        return this.sex;
    }

    public String getRegion() {
        return this.region;
    }

    public String getBio() {
        return this.bio;
    }

    public String getPfp(){
        return this.pfp;
    }

    public String getGroups(){
        return this.groups;
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

    public void setAge(int n) {
        this.age = n;
    }

    public void setSex(String p) {
        this.sex = p;
    }

    public void setRegion(String i){
        this.region = i;
    }

    public void setBio(String n) {
        this.bio = n;
    }

    public void setPfp(String p) {
        this.pfp = p;
    }

    public void setGroups(String i){
        this.groups = i;
    }
}