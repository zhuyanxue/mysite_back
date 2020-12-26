package com.mysite.api.dao;

import com.mysite.api.pojo.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppDao extends JpaRepository<App,Integer> {

}
