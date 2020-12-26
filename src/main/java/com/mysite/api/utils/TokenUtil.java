package com.mysite.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mysite.api.pojo.Users;

import java.util.Date;

public class TokenUtil {
    /**
     * 生成token 的方法
     * @param user
     * @return
     */
    public static String  getToken(Users user){
        String token = "";

        String Id= String.valueOf(user.getId());
        //设置token中要存放的信息，邮箱。应该存放ID
        token = JWT.create().withAudience(Id)
                .sign(Algorithm.HMAC256(user.getPassword()));//密钥签名，
        return token;
    }

}
