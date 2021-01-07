package com.mysite.api.dao;

import com.mysite.api.pojo.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeDao extends JpaRepository<Code,Integer> {
    Code findByIcode(String code);
    Code findById(int id);
}
