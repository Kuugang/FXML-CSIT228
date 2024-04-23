package com.example.activityfxml.Entities;

public class User {
    public static Integer id;
    public static String firstname;
    public static String lastname;
    public static String email;
    public static String username;

    public void clear(){
        id = Integer.parseInt(null);
        firstname = null;
        lastname= null;
        email= null;
        username= null;
    }
}
