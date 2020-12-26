package com.mysite.api.dao;

import com.mysite.api.pojo.Block;
import com.mysite.api.pojo.MinFrame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MinFrameDao extends JpaRepository<MinFrame,Integer> {
    List<MinFrame> findAllByBlock(Block block);
    MinFrame findById(int id);
}
