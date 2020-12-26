package com.mysite.api.dao;

import com.mysite.api.pojo.App;
import com.mysite.api.pojo.Frame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FrameDao extends JpaRepository<Frame,Integer> {
    //复杂查询才写
    List<Frame> findAllByApp(App app);
}
