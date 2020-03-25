package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/json")
public class Contorller {
    @RequestMapping("/user")
    public User getUser(){
        return new User(1,"高士玮","123456");
    }
    @RequestMapping("/list")
    public List<User> getUserList(){
        List<User> userList=new ArrayList<>();
        User user1=new User(1,"高士玮","123456");
        User user2=new User(2,"拍桌子","123456");
        userList.add(user1);
        userList.add(user2);
        return userList;
    }
    @RequestMapping("/map")
    public Map<String,Object> getMap(){
        Map<String,Object> map=new HashMap<>(3);
        User user=new User(1,"高士玮","123456");
        map.put("作者信息",user);
        map.put("博客地址","http://blog.itcodai.com");
        map.put("CSDN地址","http://blog.csdn.net/eson_15");
        map.put("粉丝数量",4153);
        return map;
    }
}
