package com.mysite.api.service;

import com.mysite.api.dao.BlockDao;
import com.mysite.api.pojo.Block;
import com.mysite.api.pojo.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {
    @Autowired
    BlockDao blockDao;

    //通过frame。
    //查询
    public List<Block> findAllBlockByFrame(Frame frame){
        return  blockDao.findAllByFrame(frame);
    }

    public Block findOneById(int id){
        return blockDao.getOne(id);
    }

    public void saveBlock(Block block){
        blockDao.save(block);
    }

    public void deleteBlockById(int id){
        blockDao.deleteById(id);
    }

}
