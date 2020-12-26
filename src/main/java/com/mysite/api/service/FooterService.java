package com.mysite.api.service;

import com.mysite.api.dao.FooterDao;
import com.mysite.api.pojo.Footer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FooterService {
    @Autowired
    FooterDao footerDao;

    public void saveFooter(Footer footer){
        footerDao.save(footer);
    }

    public void deleteFooter(int id){
        footerDao.deleteById(id);
    }

    public List<Footer> findAllFooter(){
        return footerDao.findAll();
    }

    public List<Footer> findByStatus(int status){
       return footerDao.findAllByStatus(status);
    }

    public Footer getOneFooter(int id){
        return footerDao.getOne(id);
    }
}
