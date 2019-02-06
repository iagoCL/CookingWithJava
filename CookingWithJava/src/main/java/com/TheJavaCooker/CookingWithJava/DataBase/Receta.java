package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Receta")
@Table(name = "receta",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombreReceta"}, name = Receta.constraintNombreReceta)
        }
)
public class Receta {
    public static final String constraintNombreReceta = "CONSTRAINT_NOMBRE_RECETA_UNICA";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String tipoPlato;
    @Column(nullable = false)
    private String nombreReceta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario creadorDeLaReceta;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST
            })
    @JoinTable(name = "usuario_recetasFavoritas",
            joinColumns = @JoinColumn(name = "receta_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> favoritos = new ArrayList<>();

    //todo comentarios
    //todo ingredientes
    //todo pasos
    //todo utensilios

    //todo busqueda
    //todo fotos
    //todo formularios

    //todo paso a mysql

    //todo ejemplos inicio

    protected Receta() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipoPlato() {
        return tipoPlato;
    }

    public void setTipoPlato(String tipoPlato) {
        this.tipoPlato = tipoPlato.toLowerCase();
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta.toLowerCase();
    }

    public Usuario getCreadorDeLaReceta() {
        return creadorDeLaReceta;
    }

    public boolean marcarFavorito(Usuario usuario_) {
        if (marcadaComoFavorita(usuario_)) {
            return false;
        } else {
            favoritos.add(usuario_);
            usuario_.getRecetasFavoritas().add(this);
            return true;
        }
    }

    public boolean quitarFavorito(Usuario usuario_) {
        if (marcadaComoFavorita(usuario_)) {
            favoritos.remove(usuario_);
            usuario_.getRecetasFavoritas().remove(this);
            return true;
        } else {
            return false;
        }
    }

    public boolean marcadaComoFavorita(Usuario usuario_) {
        return favoritos.contains(usuario_);
    }

    public int getNumFavoritos() {
        return favoritos.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receta receta = (Receta) o;
        return id == receta.id;
    }

    public boolean completeEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receta receta = (Receta) o;
        return id == receta.id &&
                Objects.equals(tipoPlato, receta.tipoPlato) &&
                Objects.equals(nombreReceta, receta.nombreReceta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoPlato, nombreReceta);
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", tipoPlato='" + tipoPlato + '\'' +
                ", nombreReceta='" + nombreReceta + '\'' +
                ", creadorDeLaReceta=" + creadorDeLaReceta +
                '}';
    }

    public Receta(String nombreReceta_, String tipoPlato_, Usuario creadorDeLaReceta_) {
        this.tipoPlato = tipoPlato_.toLowerCase();
        this.nombreReceta = nombreReceta_;
        this.creadorDeLaReceta = creadorDeLaReceta_;
    }
}
