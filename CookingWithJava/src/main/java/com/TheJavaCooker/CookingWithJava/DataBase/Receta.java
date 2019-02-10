package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelDeDificultad nivelDificultad;
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario creadorDeLaReceta;

    @OneToMany(
            mappedBy = "recetaId",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<Ingrediente> ingredientes = new HashSet<>();

    @SortNatural
    @OneToMany(
            mappedBy = "recetaId",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<Paso> pasos = new TreeSet<>();

    @OneToMany(
            mappedBy = "recetaId",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<Utensilio> utensilios = new HashSet<>();

    @SortNatural
    @OneToMany(
            mappedBy = "recetaId",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<Comentario> comentarios = new TreeSet<>();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST
            })
    @JoinTable(name = "usuario_recetasFavoritas",
            joinColumns = @JoinColumn(name = "receta_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> favoritos = new ArrayList<>();


    //todo duracion total

    //todo comentarios

    //todo busqueda
    //todo fotos
    //todo formularios

    //todo paso a mysql

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

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getStringFechaCreacion() {
        return fechaCreacion.format(Usuario.formatoFecha);
    }

    public void resetFechaCreacion() {
        this.fechaCreacion = LocalDate.now();
    }

    public NivelDeDificultad getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(NivelDeDificultad nivelDificultad_) {
        this.nivelDificultad = nivelDificultad_;
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

    public String mostrarMultilinea()
    {
        String string = toString();
        string+="\nIngredientes de la receta: "+ingredientes.size();
        for( Ingrediente i : ingredientes){
            string+="\nIngrediente: "+i;
        }
        PersonalDebug.imprimir("\n\nUtensilios de la receta: "+utensilios.size());
        for( Utensilio i : utensilios){
            string+="\nUtensilio: "+i;
        }
        string+="\n\nPasos de la receta: "+pasos.size();
        for( Paso i : pasos){
            string+="\nPaso: "+i;
        }
        return string;
    }

    public Set<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public Set<Paso> getPasos() {
        return pasos;
    }

    public Set<Utensilio> getUtensilios() {
        return utensilios;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
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
                Objects.equals(fechaCreacion, receta.fechaCreacion) &&
                Objects.equals(nombreReceta, receta.nombreReceta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", tipoPlato='" + tipoPlato + '\'' +
                ", nombreReceta='" + nombreReceta + '\'' +
                ", creadorDeLaReceta=" + creadorDeLaReceta +
                ", fechaDeCreacion='" + fechaCreacion.format(Usuario.formatoFecha) + '\'' +
                '}';
    }

    public Receta(String nombreReceta_, String tipoPlato_, NivelDeDificultad nivelDeDificultad_, Usuario creadorDeLaReceta_) {
        this.tipoPlato = tipoPlato_.toLowerCase();
        this.nombreReceta = nombreReceta_;
        this.creadorDeLaReceta = creadorDeLaReceta_;
        this.nivelDificultad = nivelDeDificultad_;
        resetFechaCreacion();
    }
}
