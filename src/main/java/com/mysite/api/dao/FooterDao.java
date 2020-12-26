package com.mysite.api.dao;

import com.mysite.api.pojo.Footer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FooterDao extends JpaRepository<Footer,Integer> {
    List<Footer> findAllByStatus(int status);
}
