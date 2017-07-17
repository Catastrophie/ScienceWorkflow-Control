package edu.utah.chpc.crystal.test;

/**
 * Created by crystal on 6/9/2017.
 */

public class User {
    public long userId;
    public String username;
    public String password;

    public User(long userId, String username, String password){
        this.userId=userId;
        this.username=username;
        this.password=password;
    }

}