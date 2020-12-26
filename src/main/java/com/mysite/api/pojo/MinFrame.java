package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="minframe")
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class MinFrame{
    //private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "mintitle")
    private String minTitle;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    @JsonIgnoreProperties({"minFrameList"})
    private Block block;


    //。级联删除。在一CascadeType.REMOVE
    @JsonIgnoreProperties({"minFrame"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "minFrame", fetch = FetchType.LAZY)
    private List<Detail> detailList;

    public String getMinTitle() {
        return minTitle;
    }

    public void setMinTitle(String minTitle) {
        this.minTitle = minTitle;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
