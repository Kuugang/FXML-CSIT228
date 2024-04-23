package com.example.activityfxml.Entities;

public class Note {
    public int id;
    public int userid;
    public String note;
    public String createdAt;
    public String updatedAt;

    public Note(int id, int userid, String note, String createdAt, String updatedAt){
        this.id = id;
        this.userid = userid;
        this.note = note;
        this.createdAt= createdAt;
        this.updatedAt= updatedAt;
    }
}
