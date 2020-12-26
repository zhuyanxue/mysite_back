package com.mysite.api.config;

import lombok.Data;

@Data
public class Result {
    public static String SUCCESS_CODE="0000";//成功
    public static String FAIL_CODE="1111";//失败

    String code;//状态码
    String message;//提示信息
    Object data;//返回的数据

    //构造方法
    public Result(String code,String message,Object data){
        this.code=code;
        this.data=data;
        this.message=message;
    }

    //由类去调用静态方法
    //1、成功返回。无数据：增、删、改。仅状态码
    public static Result sucess(){
        return new Result(SUCCESS_CODE,null,null);
    }

    //2、成功返回，有数据：查询
    public static Result sucess(Object data){
        return new Result(SUCCESS_CODE,null,data);
    }

    //2、成功返回，有数据。token+id
    public static Result sucess(Object data,String message){
        return new Result(SUCCESS_CODE,message,data);
    }

    //3、返回失败;无数据
    public static Result fail(String message){
        return new Result(FAIL_CODE,message,"");
    }

    //返回失败，有数据
    public static Result fail(String message,Object data){
        return new Result(FAIL_CODE,message,data);
    }
}
