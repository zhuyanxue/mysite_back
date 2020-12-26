package com.mysite.api.service;

import com.mysite.api.dao.UserDao;
import com.mysite.api.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    //登录
    public Users login(String name,String password){
        Users user=userDao.findByNameAndPassword(name,password);
        return user;
    }

    //登录
    public Users getUser(int id){
      return    userDao.getOne(id);
    }

    public List<Users> findByEmail(String email){
        return userDao.findByEmail(email);
    }

    public void save(Users users){
        userDao.save(users);
    }


    //查找所以
    public List<Users> findAllUser(){
        return userDao.findAll();
    }


    public List<Users> searchByNameKey(String key){
        return userDao.findAllByNameLike(key);
    }
}
