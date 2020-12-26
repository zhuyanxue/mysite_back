package com.mysite.api.service;

import com.mysite.api.dao.DeskImgDao;
import com.mysite.api.pojo.DeskImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskImgService {
    @Autowired
    DeskImgDao deskImgDao;

    public List<DeskImg> findByStatus(int status){
        return deskImgDao.findByStatus(status);
    }

    public void saveDeskImg(DeskImg deskImg){
        deskImgDao.save(deskImg);
    }

    public void deleteDeskImgById(int id){
        deskImgDao.deleteById(id);
    }

    public List<DeskImg> findAllDeskImg(){
        return deskImgDao.findAll();
    }

    public DeskImg findById(int id){
        return deskImgDao.getOne(id);
    }
}
