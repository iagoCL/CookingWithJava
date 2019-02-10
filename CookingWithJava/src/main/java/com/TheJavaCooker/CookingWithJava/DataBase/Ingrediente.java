package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Ingrediente")
@Table(name = "ingrediente",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombreIngrediente", "receta_id"}, name = Ingrediente.constraintNombreIngrediente)
        }
)
public class Ingrediente {
    public static final String constraintNombreIngrediente = "CONSTRAINT_NOMBRE_INGREDIENTE_UNICO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String nombreIngrediente;
    @Column(nullable = false)
    private String cantidadIngrediente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta recetaId;

    public Ingrediente(String nombreIngrediente, String cantidadIngrediente, Receta recetaId) {
        this.nombreIngrediente = nombreIngrediente.toLowerCase();
        this.cantidadIngrediente = cantidadIngrediente;
        this.recetaId = recetaId;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "id=" + id +
                ", nombreIngrediente='" + nombreIngrediente + '\'' +
                ", cantidadIngrediente='" + cantidadIngrediente + '\'' +
                ", recetaId=" + recetaId.getId() +
                '}';
    }

    protected Ingrediente(){}

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
        return nombreIngrediente;
    }

    public String getCantidadIngrediente() {
        return cantidadIngrediente;
    }
}
