package com.mysite.api.service;

import com.mysite.api.dao.VideoDao;
import com.mysite.api.pojo.Block;
import com.mysite.api.pojo.Videos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
    @Autowired
    VideoDao videoDao;

    public List<Videos> findAllByBlcok(Block block){
        return videoDao.findAllByBlock(block);
    }
    public List<Videos> findByBlcokAndStatusOne(Block block,int status){
        List<Videos> videosList = videoDao.findAllByBlockAndStatus(block,status);
        return videosList;
    }

    public Videos findOneById(int id){
        return videoDao.getOne(id);
    }

    public void saveVideo(Videos videos){
        int judge=videos.getStatus();
        if(judge==0){
            videoDao.save(videos);
        }else{
            List<Videos> videosList = videoDao.findAllByBlockAndStatus(videos.getBlock(),judge);
            for (Videos video : videosList) {
                video.setStatus(0);
            };
            videoDao.save(videos);
        }
    }

    public void deleteVideo(int id){
        videoDao.deleteById(id);
    }
}
