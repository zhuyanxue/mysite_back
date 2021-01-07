package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//模块
@Entity
@Table(name="block")
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Block{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "describes")
    private String describes;

    @Column(name = "progress")
    private int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frameid")
    @JsonIgnoreProperties({"blockList"})
    private Frame frame;


    //。级联删除。在一CascadeType.REMOVE
    @JsonIgnoreProperties({"block"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "block", fetch = FetchType.LAZY)
    private List<MinFrame> minFrameList;


    @JsonIgnoreProperties({"block"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "block", fetch = FetchType.LAZY)
    private List<Videos> videosList;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }



    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }
}
