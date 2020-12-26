package com.mysite.api.service;

import com.mysite.api.dao.AppDao;
import com.mysite.api.pojo.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {
    @Autowired
    AppDao appDao;

    public void save(App app){
        appDao.save(app);
    }

    public List<App> findAllApp() {
        return appDao.findAll();
    }

    public App findById(int id) {
        return appDao.getOne(id);
    }

    public void deleteAppById(int id) {
        appDao.deleteById(id);
    }
}
