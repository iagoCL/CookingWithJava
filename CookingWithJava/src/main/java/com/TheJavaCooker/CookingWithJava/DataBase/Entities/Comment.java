package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Table(name = "Comment", uniqueConstraints = { @UniqueConstraint(columnNames = { "comment_date", "user_id",
        "recipe_id" }, name = Comment.uniqueCommentConstraint) })
@Entity(name = "Comment")
public class Comment implements Comparable<Comment>, Serializable {

    public static final String uniqueCommentConstraint = "UNIQUE_CONSTRAINT_COMMENT";
    public static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd LLL yy - HH:MM");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false, name = "comment_description")
    private String comment_description;
    @Column(nullable = false, name = "comment_title")
    private String comment_title;
    @Column(nullable = false, name = "comment_date")
    private LocalDateTime comment_date;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe_id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user_id;

    protected Comment() {
    }

    public LocalDateTime getCommentDate() {
        return comment_date;
    }

    public void resetCommentDate() {
        setCommentDate(LocalDateTime.now());
    }

    public void setCommentDate(LocalDateTime comment_date_) {
        this.comment_date = comment_date_.minusNanos(comment_date_.getNano())
                .minusSeconds(comment_date_.getSecond() % 20);
    }

    public String getCommentDescription() {
        return comment_description;
    }

    public String getCommentTitle() {
        return comment_title;
    }

    public Comment(String comment_description, String comment_title, LocalDateTime comment_date, Recipe recipe_id,
            User user_id) {
        this.comment_description = comment_description;
        this.comment_title = comment_title;
        setCommentDate(comment_date);
        this.recipe_id = recipe_id;
        this.user_id = user_id;
    }

    public User getUser() {
        return user_id;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", comment_description='" + comment_description + '\'' + ", comment_title='"
                + comment_title + '\'' + ", comment_date=" + comment_date.format(dateTimeFormat) + ", recipe_id="
                + recipe_id.getId() + ", user_id=" + user_id.getId() + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        Comment that = (Comment) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Comment other_) {
        if (other_ == null || other_.comment_date == null)
            return 1;
        int date = other_.comment_date.compareTo(this.comment_date);
        if (date == 0) {
            return (int) (this.id - other_.id);
        } else {
            return date;
        }
    }
}