package com.collaboration;

public class Users {
    private int id;
    private  String name;

        public Users(int idIn,String nameIn){
            this.id=idIn;
            this.name=nameIn;
        }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
