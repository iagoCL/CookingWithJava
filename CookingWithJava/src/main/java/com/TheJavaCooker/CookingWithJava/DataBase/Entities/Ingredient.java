package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity(name = "Ingredient")
@Table(name = "Ingredient", uniqueConstraints = { @UniqueConstraint(columnNames = { "ingredient_name",
        "recipe_id" }, name = Ingredient.constraintUniqueIngredientName) })
public class Ingredient implements Serializable {
    public static final String constraintUniqueIngredientName = "CONSTRAINT_UNIQUE_INGREDIENT_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "ingredient_name")
    private String ingredient_name;
    @Column(nullable = false, name = "ingredient_amount")
    private String ingredient_amount;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe_id;

    public Ingredient(String ingredient_name, String ingredient_amount, Recipe recipe_id) {
        this.ingredient_name = ingredient_name.toLowerCase();
        this.ingredient_amount = ingredient_amount;
        this.recipe_id = recipe_id;
    }

    @Override
    public String toString() {
        return "Ingredient{" + "id=" + id + ", ingredient_name='" + ingredient_name + '\'' + ", ingredient_amount='"
                + ingredient_amount + '\'' + ", recipe_id=" + recipe_id.getId() + '}';
    }

    protected Ingredient() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Ingredient that = (Ingredient) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getIngredientName() {
        return ingredient_name;
    }

    public String getIngredientAmount() {
        return ingredient_amount;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> map = new HashMap<>();
        map.put("ingredient_name", ingredient_name);
        map.put("ingredient_amount", ingredient_amount);
        return map;
    }

}