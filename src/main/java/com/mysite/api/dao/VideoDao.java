package com.mysite.api.dao;

import com.mysite.api.pojo.Block;
import com.mysite.api.pojo.Videos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoDao extends JpaRepository<Videos,Integer> {
    List<Videos> findAllByBlock(Block block);
    List<Videos> findAllByBlockAndStatus(Block block,int status);
}
