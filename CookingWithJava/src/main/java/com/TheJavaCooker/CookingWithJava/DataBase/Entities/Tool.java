package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.TheJavaCooker.CookingWithJava.DataBase.DifficultyLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(name = "Tool")
@Table(name = "Tool", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "tool_name", "recipe_id" }, name = Tool.constraintToolName) })
public class Tool implements Serializable {
    public static final String constraintToolName = "CONSTRAINT_UNIQUE_TOOL_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "tool_name")
    private String tool_name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "difficulty_level")
    private DifficultyLevel difficulty_level;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe_id;

    protected Tool() {
    }

    public String getToolName() {
        return tool_name;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficulty_level;
    }

    @Override
    public String toString() {
        return "Tool{" + "id=" + id + ", tool_name='" + tool_name + '\'' + ", difficulty_level=" + difficulty_level
                + ", recipe_id=" + recipe_id.getId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Tool tool = (Tool) obj;
        return id == tool.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Tool(String tool_name, DifficultyLevel difficulty_level_, Recipe recipe_id_) {
        this.tool_name = tool_name.toLowerCase();
        this.difficulty_level = difficulty_level_;
        this.recipe_id = recipe_id_;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> map = new HashMap<>();
        map.put("tool_name", tool_name);
        map.put("difficulty_level", difficulty_level);
        return map;
    }
}