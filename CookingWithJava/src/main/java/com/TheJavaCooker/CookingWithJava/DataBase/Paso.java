package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Paso")
@Table(name = "paso",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"numPaso", "receta_id"}, name = Paso.constraintNombrePaso)
        }
)
public class Paso implements Comparable<Paso> {
    public static final String constraintNombrePaso = "CONSTRAINT_NOMBRE_PASO_UNICO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false)
    private String descripcionPaso;
    @Column(nullable = false)
    private int numPaso;
    @Column(nullable = false)
    private int duracion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta recetaId;

    public Paso(int numPaso_, int duracion_, String descripcionPaso_, Receta recetaId_) {
        this.descripcionPaso = descripcionPaso_;
        this.numPaso = numPaso_;
        this.duracion = duracion_;
        this.recetaId = recetaId_;
    }

    protected Paso() {
    }

    @Override
    public String toString() {
        return "Paso{" +
                "id=" + id +
                ", numPaso='" + numPaso + '\'' +
                ", duracion=" + getDuracionString() +
                ", recetaId=" + recetaId.getId() +
                ", descripcionPaso=" + descripcionPaso +
                '}';
    }

    public String getDescripcionPaso() {
        return descripcionPaso;
    }

    public int getNumPaso() {
        return numPaso;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getDuracionString() {
        return formatearTiempo(duracion);
    }

    public static String formatearTiempo(int tiempo) {
        if (tiempo < 60) {
            return tiempo + " min";
        } else {
            int minutos = tiempo % 60;
            int horas = tiempo / 60;
            if (minutos == 0) {
                return horas + "h";
            } else {
                return horas + " h " + minutos + " min";
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paso paso = (Paso) o;
        return id == paso.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Paso other_) {
        return this.numPaso - other_.numPaso;
    }
}
