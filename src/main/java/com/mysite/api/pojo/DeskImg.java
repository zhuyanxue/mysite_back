package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel
@Entity
@Table(name="deskimg")
@Data   //get、set
@AllArgsConstructor //所有参数构造方法
@NoArgsConstructor  //无参数、
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class DeskImg{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private int status;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
