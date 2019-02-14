package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Utensilio")
@Table(name = "Utensilio",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombre_utensilio", "receta_id"}, name = Utensilio.constraintNombreUtensilio)
        }
)
public class Utensilio {
    public static final String constraintNombreUtensilio = "CONSTRAINT_NOMBRE_UTENSILIO_UNICO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "nombre_utensilio")
    private String nombre_utensilio;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "nivel_de_dificultad")
    private NivelDeDificultad nivel_de_dificultad;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta_id;

    protected Utensilio() {
    }

    public String getNombreUtensilio() {
        return nombre_utensilio;
    }

    public NivelDeDificultad getNivelDeDificultad() {
        return nivel_de_dificultad;
    }

    @Override
    public String toString() {
        return "Utensilio{" +
                "id=" + id +
                ", nombre_utensilio='" + nombre_utensilio + '\'' +
                ", nivel_de_dificultad=" + nivel_de_dificultad +
                ", receta_id=" + receta_id.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utensilio utensilio = (Utensilio) o;
        return id == utensilio.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Utensilio(String nombre_utensilio_, NivelDeDificultad nivel_de_dificultad_, Receta receta_id_) {
        this.nombre_utensilio = nombre_utensilio_.toLowerCase();
        this.nivel_de_dificultad = nivel_de_dificultad_;
        this.receta_id = receta_id_;
    }
}
