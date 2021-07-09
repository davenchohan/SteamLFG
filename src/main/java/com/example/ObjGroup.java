package com.example;

public class ObjGroup {
    private String groupname;
    private int[] members = {0,0,0,0,0,0,0,0,0,0,0,0};
    private int maxmembers;
    private String game;
    private int gid;

    public int[] getMembers() {
        return this.members;
    }

    public int getMember(int i) {
        return this.members[i];
    }

    public String getGame() {
        return this.game;
    }

    public int getMaxmembers() {
        return this.maxmembers;
    }

    public String getGroupname() {
        return this.groupname;
    }

    public int getGid(){
        return this.gid;
    }

    public void setMembers(int[] n) {
        this.members = n;
        for(int i = 0; i < maxmembers; i++){
            this.members[i] = n[i];
        }
    }

    public void setMember(int index, int id) {
        this.members[index] = id;
    }

    public void setGame(String n){
        this.game = n;
    }

    public void setMaxmembers(int n) {
        this.maxmembers = n;
    }

    public void setGroupname(String p) {
        this.groupname = p;
    }

    public void setGid(int id){
        this.gid = id;
    }
}