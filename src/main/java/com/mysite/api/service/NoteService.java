package com.mysite.api.service;

import com.mysite.api.dao.NoteDao;
import com.mysite.api.pojo.Detail;
import com.mysite.api.pojo.Note;
import com.mysite.api.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    NoteDao noteDao;

    public List<Note> findAllByDeAndUser(Detail detail, Users user){
        return noteDao.findAllByDetailAndUser(detail,user);
    }

    public List<Note> findAllByDe(Detail detail){
        return noteDao.findAllByDetail(detail);
    }

    public void saveNote(Note note){
        noteDao.save(note);
    }

    public Note findOneById(int id){
        return noteDao.getOne(id);
    }

    public void deleteNote(int id){
        noteDao.deleteById(id);
    }
}
