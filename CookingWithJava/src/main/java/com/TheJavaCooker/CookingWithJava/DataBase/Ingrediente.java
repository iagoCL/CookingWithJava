package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;

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

    protected Ingrediente(){}
}
