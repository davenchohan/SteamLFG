package com.example;

public class User {
    private String username;
    private String password;
    private int age;
    private String gender;
    private String region;
    private String bio;
    private String pfp;
    private String groups;
    private int id;
    private String access;
    private String adminkey;

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

    public String getGender() {
        return this.gender;
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

    public String getAccess(){
        return this.access;
    }

    public String getAdminkey(){
        return this.adminkey;
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

    public void setGender(String p) {
        this.gender = p;
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

    public void setAccess(String i){
        this.access = i;
    }

    public void setAdminkey(String i){
        this.adminkey = i;
    }
}