package com.mysite.api.dao;

import com.mysite.api.pojo.Detail;
import com.mysite.api.pojo.Note;
import com.mysite.api.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteDao extends JpaRepository<Note,Integer> {
    List<Note> findAllByDetailAndUser(Detail detail, Users users);
    List<Note> findAllByDetail(Detail detail);
}
