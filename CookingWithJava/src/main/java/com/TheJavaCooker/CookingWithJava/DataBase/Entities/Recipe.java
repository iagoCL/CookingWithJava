package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.TheJavaCooker.CookingWithJava.DataBase.DifficultyLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SortNatural;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity(name = "Recipe")
@QueryEntity
@Table(name = "Recipe", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "recipe_name" }, name = Recipe.constraintRecipeName) })
public class Recipe implements Serializable {
    public static final String constraintRecipeName = "CONSTRAINT_UNIQUE_RECIPE_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "food_type")
    private String food_type;
    @Column(nullable = false, name = "recipe_name")
    private String recipe_name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "difficulty_level")
    private DifficultyLevel difficulty_level;
    @Column(nullable = false, name = "creation_date")
    private LocalDate creation_date;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imagedb_id")
    private Imagedb imagedb_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User recipe_creator;

    @OneToMany(mappedBy = "recipe_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Ingredient> ingredients = new HashSet<>();

    @SortNatural
    @OneToMany(mappedBy = "recipe_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Step> steps = new TreeSet<>();

    @OneToMany(mappedBy = "recipe_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Tool> tools = new HashSet<>();

    @SortNatural
    @OneToMany(mappedBy = "recipe_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Comment> comments = new TreeSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
    @JoinTable(name = "user_favorite_recipes", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> favorites = new ArrayList<>();

    // @Formula(value = "select sum(p.duration) from Step p where p.recipe_id = id")
    @Column(nullable = false, name = "total_duration")
    private int total_duration;
    @Column(nullable = false, name = "steps_number")
    private int steps_number;
    @Column(nullable = false, name = "number_comments")
    private int number_comments;
    @Column(nullable = false, name = "number_favorites")
    private int number_favorites;

    public Recipe() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalDuration() {
        return total_duration;
    }

    public String getStringTotalDuration() {
        return Step.formatTime(total_duration);
    }

    public int getNumSteps() {
        return steps_number;
    }

    public void updateNumSteps() {
        this.steps_number = steps.size();
        int total = 0;
        for (Step step : getSteps()) {
            total += step.getDuration();
        }
        this.total_duration = total;
    }

    public int getNumComments() {
        return number_comments;
    }

    public void newComment() {
        ++this.number_comments;
    }

    public String getFoodType() {
        return food_type;
    }

    public void setFoodType(String food_type) {
        this.food_type = food_type.toLowerCase();
    }

    public String getRecipeName() {
        return recipe_name;
    }

    public void setRecipeName(String recipe_name) {
        this.recipe_name = recipe_name.toLowerCase();
    }

    public LocalDate getCreationDate() {
        return creation_date;
    }

    public String getStringCreationDate() {
        return creation_date.format(User.dateFormat);
    }

    public void setCreationDate() {
        this.creation_date = LocalDate.now();
    }

    public DifficultyLevel getDifficultLevel() {
        return difficulty_level;
    }

    public void setDifficultLevel(DifficultyLevel difficulty_level_) {
        this.difficulty_level = difficulty_level_;
    }

    @Transactional
    public User getRecipeCreator() {
        Hibernate.initialize(this.recipe_creator);
        return recipe_creator;
    }

    public long getRecipeCreatorId() {
        return recipe_creator.getId();
    }

    public long getRecipeImage() {
        return imagedb_id.getId();
    }

    public boolean markAsFavorite(User user_) {
        if (isFavorite(user_)) {
            return false;
        } else {
            favorites.add(user_);
            ++number_favorites;
            return true;
        }
    }

    public boolean removeFavorite(User user_) {
        if (isFavorite(user_)) {
            favorites.remove(user_);
            --number_favorites;
            return true;
        } else {
            return false;
        }
    }

    public String printMultiLine() {
        String string = toString();
        string += "\nRecipe Ingredients: " + ingredients.size();
        for (Ingredient i : getIngredients()) {
            string += "\n " + i;
        }
        string += "\n\nRecipe Tools: " + tools.size();
        for (Tool i : getTools()) {
            string += "\n " + i;
        }
        string += "\n\nRecipe Steps: " + steps.size();
        for (Step i : getSteps()) {
            string += "\n " + i;
        }
        return string;
    }

    @Transactional
    public Set<Ingredient> getIngredients() {
        Hibernate.initialize(this.ingredients);
        return ingredients;
    }

    @Transactional
    public Set<Step> getSteps() {
        Hibernate.initialize(this.steps);
        return steps;
    }

    @Transactional
    public Set<Tool> getTools() {
        Hibernate.initialize(this.tools);
        return tools;
    }

    @Transactional
    public Set<Comment> getComments() {
        Hibernate.initialize(this.comments);
        return comments;
    }

    public boolean isFavorite(User user_) {
        return favorites.contains(user_);
    }

    public int getNumFavorites() {
        return favorites.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id;
    }

    public boolean completeEquals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id && Objects.equals(food_type, recipe.food_type)
                && Objects.equals(recipe_name, recipe.recipe_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe{" + "id=" + id + ", food_type='" + food_type + '\'' + ", recipe_name='" + recipe_name + '\''
                + ", recipe_creator=" + recipe_creator.getId() + ", total_duration=" + getStringTotalDuration()
                + ", steps_number=" + steps_number + ", number_favorites=" + number_favorites + ", difficulty_level="
                + difficulty_level.toString() + ", number_comments=" + number_comments + ", creationDate='"
                + creation_date.format(User.dateFormat) + '\'' + '}';
    }

    public Recipe(String recipe_name_, String food_type_, DifficultyLevel difficulty_level_, LocalDate creation_date_,
            Imagedb image_recipe_, User recipe_creator_) {
        this.food_type = food_type_.toLowerCase();
        this.recipe_name = recipe_name_;
        this.recipe_creator = recipe_creator_;
        this.difficulty_level = difficulty_level_;
        this.number_comments = this.number_favorites = this.total_duration = this.steps_number = 0;
        this.creation_date = creation_date_;
        imagedb_id = image_recipe_;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> map = new HashMap<>();
        map.put("food_type", food_type);
        map.put("recipe_name", recipe_name);
        map.put("difficulty_level", difficulty_level.toString());
        map.put("number_comments", number_comments);
        map.put("number_favorites", number_favorites);
        map.put("total_duration", getStringTotalDuration());

        map.put("creator", getRecipeCreator().toJSON());

        map.put("steps_number", steps_number);
        Map<String, Object> mapAux = new HashMap<>();
        for (Step step : getSteps()) {
            mapAux.put("step-" + step.getNumStep(), step.toJSON());
        }
        map.put("steps", mapAux);

        mapAux = new HashMap<>();
        int i = 0;
        for (Ingredient ingredient : getIngredients()) {
            ++i;
            mapAux.put("ingredient-" + i, ingredient.toJSON());
        }
        map.put("ingredients_number", i);
        map.put("ingredients", mapAux);

        mapAux = new HashMap<>();
        i = 0;
        for (Tool tool : getTools()) {
            ++i;
            mapAux.put("tool-" + i, tool.toJSON());
        }
        map.put("tool_number", i);
        map.put("tools", mapAux);
        return map;
    }
}