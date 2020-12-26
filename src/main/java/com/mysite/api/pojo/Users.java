package com.mysite.api.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="users")
@Data   //get、set
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;


    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @Fetch(FetchMode.SELECT)
    @JsonIgnoreProperties({"users"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "users", fetch = FetchType.LAZY)
    @JsonBackReference(value = "appList")
    private List<App> appList;

    @Fetch(FetchMode.SELECT)
    @JsonIgnoreProperties({"user"})
    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference(value = "noteList")
    private List<Note> noteList;



    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
