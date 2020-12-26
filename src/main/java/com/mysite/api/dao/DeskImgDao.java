package com.mysite.api.dao;

import com.mysite.api.pojo.DeskImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeskImgDao extends JpaRepository<DeskImg,Integer> {
    List<DeskImg> findByStatus(int status);
}
