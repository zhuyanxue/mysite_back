package com.mysite.api.dao;

import com.mysite.api.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  UserDao extends JpaRepository<Users,Integer> {
    Users findByNameAndPassword(String name,String password);
    List<Users> findByEmail(String email);//一个邮箱可能多个用户。

    @Query(value = "select u from Users u where u.name like %?1%")
    List<Users> findAllByNameLike(String key);
}
