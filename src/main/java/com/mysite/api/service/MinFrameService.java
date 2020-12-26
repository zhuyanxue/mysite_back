package com.mysite.api.service;

import com.mysite.api.dao.MinFrameDao;
import com.mysite.api.pojo.Block;
import com.mysite.api.pojo.MinFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.util.List;

@Service
/*@CacheConfig(cacheNames="minFrames")*/
public class MinFrameService {
    @Autowired
    MinFrameDao minFrameDao;


    public List<MinFrame> findAllByBlock(Block block){
        return  minFrameDao.findAllByBlock(block);
    }

    //@Cacheable(key="'minFrames-one-'+ #p0")
    public MinFrame getOneById(int id){
        return minFrameDao.findById(id);
    }

    //@CacheEvict(allEntries=true)
    public void deleteMinFrame(int id){
        minFrameDao.deleteById(id);
    }

   // @CacheEvict(allEntries=true)
    public void saveMinFrame(MinFrame minFrame){
        minFrameDao.save(minFrame);
    }

}
