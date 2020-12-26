package com.mysite.api.service;

import com.mysite.api.dao.FrameDao;
import com.mysite.api.pojo.App;
import com.mysite.api.pojo.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameService {
    @Autowired
    FrameDao frameDao;

    //查询所有导航
    public List<Frame> findAllByApp(App app){
        return frameDao.findAllByApp(app);
    }

    //新增、修改。save
    public void  saveFrame(Frame frame){
        frameDao.save(frame);
    }

    //查询一个
    public Frame findById(int id){
        return frameDao.getOne(id);
    }

    //删除
    public void deleteFrame(int id){
        frameDao.deleteById(id);
    }

}
