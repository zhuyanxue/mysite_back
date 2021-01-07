package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//应用
@Entity
@Table(name="app")
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class App{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Column(name = "appname")
    private String appName;

    @Column(name = "img")
    private String img;

    @Column(name = "status")
    private String status;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties({"appList"})
    private Users users;

    @JsonIgnoreProperties({"app"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "app", fetch = FetchType.LAZY)
    private List<Frame> frameList;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
