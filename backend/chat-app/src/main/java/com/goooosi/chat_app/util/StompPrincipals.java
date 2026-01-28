package com.goooosi.chat_app.util;

import java.security.Principal;

public class StompPrincipals implements Principal {
    private final String name;


    public StompPrincipals(String name) {
        this.name = name;
    }

    @Override
    public String getName(){
        return name;
    }
}
