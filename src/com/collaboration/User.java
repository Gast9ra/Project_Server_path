package com.collaboration;

public class User {
    private int idUser;
    private String nameUser;
    private String leaderUser;
    private String userText;
    private String userProj;


    public User(int id, String name,String text,String userLeader,String userConnect) {
        this.idUser=id;
        this.nameUser=name;
        this.userText=text;
        this.leaderUser=userLeader;
        this.userProj=userConnect;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getLeaderUser() {
        return leaderUser;
    }

    public String getUserText() {
        return userText;
    }

    public String getUserProj() {
        return userProj;
    }
}
