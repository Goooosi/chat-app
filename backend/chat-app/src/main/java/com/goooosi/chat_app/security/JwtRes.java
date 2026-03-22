package com.goooosi.chat_app.security;


import lombok.Getter;

@Getter
public class JwtRes {
    private String msg;

    public JwtRes(String msg){
        this.msg = msg;
    }

}
