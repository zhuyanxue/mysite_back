package com.mysite.api.dao;

import com.mysite.api.pojo.Block;
import com.mysite.api.pojo.Frame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockDao extends JpaRepository<Block,Integer> {
    List<Block> findAllByFrame(Frame frame);
}
