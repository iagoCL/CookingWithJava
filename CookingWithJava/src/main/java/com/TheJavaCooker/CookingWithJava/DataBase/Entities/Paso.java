package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(name = "Paso")
@Table(name = "Paso",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"numero_paso", "receta_id"}, name = Paso.constraintNombrePaso)
        }
)
public class Paso  implements Serializable, Comparable<Paso> {
    public static final String constraintNombrePaso = "CONSTRAINT_NOMBRE_PASO_UNICO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false, name = "descripcion_paso")
    private String descripcion_paso;
    @Column(nullable = false, name = "numero_paso")
    private int numero_paso;
    @Column(nullable = false, name = "duracion")
    private int duracion;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta_id;

    public Paso(int numero_paso_, int duracion_, String descripcion_paso_, Receta receta_id_) {
        this.descripcion_paso = descripcion_paso_;
        this.numero_paso = numero_paso_;
        this.duracion = duracion_;
        this.receta_id = receta_id_;
    }

    protected Paso() {
    }

    @Override
    public String toString() {
        return "Paso{" +
                "id=" + id +
                ", numero_paso='" + numero_paso + '\'' +
                ", duracion=" + getDuracionString() +
                ", receta_id=" + receta_id.getId() +
                ", descripcion_paso=" + descripcion_paso +
                '}';
    }

    public String getDescripcionPaso() {
        return descripcion_paso;
    }

    public int getNumPaso() {
        return numero_paso;
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
        return this.numero_paso - other_.numero_paso;
    }

    public Map<String,Object> toJSON(){
        Map<String,Object> map = new HashMap<>();
        map.put("duracion",getDuracionString());
        map.put("descripcion",descripcion_paso);
        return map;
    }
}
