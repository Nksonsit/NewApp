package com.myapp.newapp.api.model;

/**
 * Created by ishan on 19-09-2017.
 */

public class LoginReq {
    private String Email;
    private String Password;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
