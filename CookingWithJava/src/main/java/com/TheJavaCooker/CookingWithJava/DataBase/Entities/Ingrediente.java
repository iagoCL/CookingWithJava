package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(name = "Ingrediente")
@Table(name = "Ingrediente",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombre_ingrediente", "receta_id"}, name = Ingrediente.constraintNombreIngrediente)
        }
)
public class Ingrediente {
    public static final String constraintNombreIngrediente = "CONSTRAINT_NOMBRE_INGREDIENTE_UNICO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "nombre_ingrediente")
    private String nombre_ingrediente;
    @Column(nullable = false, name = "cantidad_ingrediente")
    private String cantidad_ingrediente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta_id;

    public Ingrediente(String nombre_ingrediente, String cantidad_ingrediente, Receta receta_id) {
        this.nombre_ingrediente = nombre_ingrediente.toLowerCase();
        this.cantidad_ingrediente = cantidad_ingrediente;
        this.receta_id = receta_id;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "id=" + id +
                ", nombre_ingrediente='" + nombre_ingrediente + '\'' +
                ", cantidad_ingrediente='" + cantidad_ingrediente + '\'' +
                ", receta_id=" + receta_id.getId() +
                '}';
    }

    protected Ingrediente() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingrediente that = (Ingrediente) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getNombreIngrediente() {
        return nombre_ingrediente;
    }

    public String getCantidadIngrediente() {
        return cantidad_ingrediente;
    }

    public Map<String,Object> toJSON(){
        Map<String,Object> map = new HashMap<>();
        map.put("nombre_ingrediente",nombre_ingrediente);
        map.put("cantidad_ingrediente",cantidad_ingrediente);
        return map;
    }

}
