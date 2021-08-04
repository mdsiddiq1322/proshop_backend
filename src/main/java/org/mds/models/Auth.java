package org.mds.models;

public class Auth {
    String role;
    String email;
    String password;

    public String getRole(){
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
