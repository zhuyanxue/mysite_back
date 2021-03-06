package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="detail")
@Data
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@Document(indexName = "mysite")
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private int id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    @Column(name = "title")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    @Column(name = "details")
    private String details;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mfid")
    @JsonIgnoreProperties({"detailList"})
    private MinFrame minFrame;


    //。级联删除。在一CascadeType.REMOVE
    @JsonIgnoreProperties({"detail"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "detail", fetch = FetchType.LAZY)
    private List<Note> noteList;

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public MinFrame getMinFrame() {
        return minFrame;
    }

    public void setMinFrame(MinFrame minFrame) {
        this.minFrame = minFrame;
    }
}
