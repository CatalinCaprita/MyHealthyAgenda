package com.example.myhealthyagenda.user;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super();
    }
    public UserNotFoundException(String message){
        super(message);
    }
}
