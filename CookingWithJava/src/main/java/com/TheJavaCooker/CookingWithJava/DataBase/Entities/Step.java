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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(name = "Step")
@Table(name = "Step", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "step_number", "recipe_id" }, name = Step.constraintStepName) })
public class Step implements Serializable, Comparable<Step> {
    public static final String constraintStepName = "CONSTRAINT_UNIQUE_STEP_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false, name = "description_step")
    private String description_step;
    @Column(nullable = false, name = "step_number")
    private int step_number;
    @Column(nullable = false, name = "duration")
    private int duration;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe_id;

    public Step(int step_number_, int duration_, String description_step_, Recipe recipe_id_) {
        this.description_step = description_step_;
        this.step_number = step_number_;
        this.duration = duration_;
        this.recipe_id = recipe_id_;
    }

    protected Step() {
    }

    @Override
    public String toString() {
        return "Step{" + "id=" + id + ", step_number='" + step_number + '\'' + ", duration=" + getDurationString()
                + ", recipe_id=" + recipe_id.getId() + ", description_step=" + description_step + '}';
    }

    public String getDescriptionStep() {
        return description_step;
    }

    public int getNumStep() {
        return step_number;
    }

    public int getDuration() {
        return duration;
    }

    public String getDurationString() {
        return formatTime(duration);
    }

    public static String formatTime(int time) {
        if (time < 60) {
            return time + " min";
        } else {
            int minutes = time % 60;
            int hours = time / 60;
            if (minutes == 0) {
                return hours + "h";
            } else {
                return hours + " h " + minutes + " min";
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Step step = (Step) obj;
        return id == step.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Step other_) {
        return this.step_number - other_.step_number;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> map = new HashMap<>();
        map.put("duration", getDurationString());
        map.put("description", description_step);
        return map;
    }
}