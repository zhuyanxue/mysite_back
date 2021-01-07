package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//导航
@Entity
@Table(name="frame")
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Frame{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "framename")
    private String frameName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appid")
    @JsonIgnoreProperties({"frameList"})
    private App app;


    //。级联删除。在一CascadeType.REMOVE
    @JsonIgnoreProperties({"frame"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "frame", fetch = FetchType.LAZY)
    private List<Block> blockList;


    public String getFrameName() {
        return frameName;
    }



    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
