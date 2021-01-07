package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//邮箱验证码
@Entity
@Table(name="code")
@Data
@AllArgsConstructor //所有参数构造方法
@NoArgsConstructor  //无参数
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Code{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "icode")
    private String icode;

    @Column(name = "sendtime")
    private Date sendTime;

    public String getIcode() {
        return icode;
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
