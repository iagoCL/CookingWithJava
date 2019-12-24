package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "User")
@Table(name = "User", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_name" }, name = User.constraintUserName),
        @UniqueConstraint(columnNames = { "mail" }, name = User.constraintUserMail) })
public class User implements Serializable {
    public static final String constraintUserName = "CONSTRAINT_UNIQUE_USER_NAME";
    public static final String constraintUserMail = "CONSTRAINT_UNIQUE_MAIL";
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd LLL yy");
    private static final String randomHash = "dseSSfsVreH435gvQEFQabHsr";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "user_name")
    private String user_name;
    @Column(nullable = false, name = "pass")
    private String pass;
    @Column(nullable = false, name = "mail")
    private String mail;
    @Column(nullable = false, name = "complete_name")
    private String complete_name;
    @Column(nullable = false, name = "creation_date")
    private LocalDate creation_date;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Imagedb image_id;

    @JsonIgnore
    @OneToMany(mappedBy = "recipe_creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Recipe> created_recipes = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favorites")
    private List<Recipe> favorite_recipes = new ArrayList<>();

    @Column(nullable = true, name = "num_comments_user")
    private int num_comments_user;

    public long getId() {
        return id;
    }

    public long getUserImage() {
        return image_id.getId();
    }

    public String getUserCompleteName() {
        return complete_name;
    }

    public void setUserCompleteName(String complete_name_) {
        this.complete_name = complete_name_;
    }

    public String getUserName() {
        return user_name;
    }

    public String getPass() {
        return pass;
    }

    public String getUserMail() {
        return mail;
    }

    public LocalDate getCreationDate() {
        return creation_date;
    }

    public String getStringCreationDate() {
        return creation_date.format(User.dateFormat);
    }

    public boolean comparePass(String pass_) {
        return this.pass.equals(codifyPass(pass_));
    }

    public void setCreationDate() {
        this.creation_date = LocalDate.now();
    }

    public void setUserName(String user_name_) {
        this.user_name = user_name_;
    }

    public void setPass(String pass_) {
        this.pass = codifyPass(pass_);
    }

    private String codifyPass(String pass_) {
        return Integer.toString((randomHash + pass_).hashCode());
    }

    public void setUserMail(String mail_) {
        this.mail = mail_;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return id == user.id;
    }

    public boolean completeEquals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return id == user.id && Objects.equals(user_name, user.user_name) && Objects.equals(pass, user.pass)
                && Objects.equals(complete_name, user.complete_name) && Objects.equals(mail, user.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", user_name='" + user_name + '\'' + ", pass='" + pass + '\'' + ", mail='" + mail
                + '\'' + ", complete_name='" + complete_name + '\'' + ", creationDate='"
                + creation_date.format(dateFormat) + '\'' + '}';
    }

    public int getNumCommentsUser() {
        return num_comments_user;
    }

    public void newComment() {
        ++num_comments_user;
    }

    public void removeComment() {
        --num_comments_user;
    }

    public User() {
    }

    public User(String user_name_, String pass_, String mail_, String complete_name_, Imagedb imagedb_id_) {
        this.user_name = user_name_;
        setPass(pass_);
        this.mail = mail_;
        this.complete_name = complete_name_;
        setCreationDate();
        image_id = imagedb_id_;
        this.num_comments_user = 0;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_name", user_name);
        map.put("mail", mail);
        map.put("complete_name", complete_name);
        return map;
    }
}