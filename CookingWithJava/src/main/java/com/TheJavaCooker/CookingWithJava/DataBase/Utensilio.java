package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Utensilio")
@Table(name = "utensilio",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombreUtensilio", "receta_id"}, name = Utensilio.constraintNombreUtensilio)
        }
)
public class Utensilio {
    public static final String constraintNombreUtensilio = "CONSTRAINT_NOMBRE_UTENSILIO_UNICO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String nombreUtensilio;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelDeDificultad nivelDeDificultad;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta recetaId;

    protected Utensilio() {
    }

    public String getNombreUtensilio() {
        return nombreUtensilio;
    }

    public NivelDeDificultad getNivelDeDificultad() {
        return nivelDeDificultad;
    }

    @Override
    public String toString() {
        return "Utensilio{" +
                "id=" + id +
                ", nombreUtensilio='" + nombreUtensilio + '\'' +
                ", nivelDeDificultad=" + nivelDeDificultad +
                ", recetaId=" + recetaId.getId() +
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

    public Utensilio(String nombreUtensilio_, NivelDeDificultad nivelDeDificultad_, Receta recetaId_) {
        this.nombreUtensilio = nombreUtensilio_.toLowerCase();
        this.nivelDeDificultad = nivelDeDificultad_;
        this.recetaId = recetaId_;
    }
}
