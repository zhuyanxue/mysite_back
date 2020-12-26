package com.mysite.api.dao;

import com.mysite.api.pojo.Detail;
import com.mysite.api.pojo.MinFrame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailDao extends JpaRepository<Detail,Integer> {
    List<Detail> findAllByMinFrame(MinFrame minFrame);
    Detail getById(int id);
}
