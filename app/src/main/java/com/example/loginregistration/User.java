package com.example.loginregistration;

public class User {
    public String fullname, phone, email, password;
    public User(){

    }
    public User(String fullnameTxt, String phoneTxt, String emailTxt, String passwordTxt){
        this.fullname = fullnameTxt;
        this.phone = phoneTxt;
        this.email = emailTxt;
        this.password = passwordTxt;
    }
}
