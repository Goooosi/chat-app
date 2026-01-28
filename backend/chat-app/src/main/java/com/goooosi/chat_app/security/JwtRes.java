package com.goooosi.chat_app.security;



public class JwtRes {
    private String token;

    public JwtRes(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
