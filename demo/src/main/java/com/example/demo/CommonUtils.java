package com.example.demo;

import java.util.UUID;

public class CommonUtils {
    public static String generateUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,32);
        return uuid;
    }

}