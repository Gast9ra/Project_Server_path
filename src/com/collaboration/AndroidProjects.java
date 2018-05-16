package com.collaboration;

import java.util.ArrayList;

public class AndroidProjects {
    private int idProject;
    private String nameProject;
    private int leaderProject;
    private ArrayList<Users> usersProject;

    public AndroidProjects() {
    }

    public AndroidProjects(int id, String name, int leader, ArrayList<Users> listUsers){
            this.idProject=id;
            this.leaderProject=leader;
            this.nameProject=name;
            this.usersProject=listUsers;
    }

    public int getIdProject() {
        return idProject;
    }

    public String getNameProject() {
        return nameProject;
    }

    public int getLeaderProject() {
        return leaderProject;
    }

    public ArrayList<Users> getUsersProject() {
        return usersProject;
    }
}
