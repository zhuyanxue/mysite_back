package com.mysite.api.service;

import com.mysite.api.dao.CodeDao;
import com.mysite.api.pojo.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeService {
    @Autowired
    CodeDao codeDao;
    public void  add(Code code){
        codeDao.save(code);
    }

    public void deleteAll(){
         codeDao.deleteAll();
    };

    public  List<Code> getAllCode(){
      return   codeDao.findAll();
    }



    public Code findByIcode(String code){
        return codeDao.findByIcode(code);
    }

    public Code findById(int id){
        return codeDao.findById(id);
    }
}
