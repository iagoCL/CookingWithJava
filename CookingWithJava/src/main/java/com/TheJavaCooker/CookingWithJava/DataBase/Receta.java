package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.DatabaseRandomData;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import com.querydsl.core.annotations.QueryEntity;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity(name = "Receta")
@QueryEntity
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
    @Lob
    @Column(nullable = false)
    private byte[] imagenReceta;

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

    //@Formula(value = "select sum(p.duracion) from Paso p where p.receta_id = id")
    @Column(nullable = false)
    private int duracionTotal;
    @Column(nullable = false)
    private int numPasos;
    @Column(nullable = false)
    private int numComentarios;
    @Column(nullable = false)
    private int numFavoritos;

    protected Receta() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDuracionTotal() {
        return duracionTotal;
    }

    public String getStringDuracionTotal() {
        return Paso.formatearTiempo(duracionTotal);
    }

    public int getNumPasos() {
        return numPasos;
    }

    public void recalcNumPasos() {
        this.numPasos = pasos.size();
        int suma = 0;
        for (Paso paso : pasos) {
            suma += paso.getDuracion();
        }
        this.duracionTotal = suma;
    }

    public int getNumComentarios() {
        return numComentarios;
    }

    public void recalcNumComentarios() {
        this.numComentarios = comentarios.size();
    }

    public void recalcNumFavoritos() {
        this.numFavoritos = favoritos.size();
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
        this.fechaCreacion = LocalDate.now();//DatabaseRandomData.getRandomDateTime().toLocalDate();//
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

    public void setImagenReceta(byte[] imagenReceta_)
    {
        if(imagenReceta_ == null || imagenReceta_.length == 0)
        {
            this.imagenReceta = DatabaseRandomData.getRandomUserImage();
        }
        else{
            this.imagenReceta = DatabaseManager.transformarAImagenDeReceta(imagenReceta_);
        }
    }

    public byte[] getImagenReceta() {
        return imagenReceta;
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

    public String mostrarMultilinea() {
        String string = toString();
        string += "\nIngredientes de la receta: " + ingredientes.size();
        for (Ingrediente i : ingredientes) {
            string += "\n " + i;
        }
        PersonalDebug.imprimir("\n\nUtensilios de la receta: " + utensilios.size());
        for (Utensilio i : utensilios) {
            string += "\n " + i;
        }
        string += "\n\nPasos de la receta: " + pasos.size();
        for (Paso i : pasos) {
            string += "\n " + i;
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
                ", creadorDeLaReceta=" + creadorDeLaReceta.getId() +
                ", duracionTotal=" + getStringDuracionTotal() +
                ", numPasos=" + numPasos +
                ", numFavoritos=" + numFavoritos +
                ", nivelDeDificultad=" + nivelDificultad.toString() +
                ", numComentarios=" + numComentarios +
                ", fechaDeCreacion='" + fechaCreacion.format(Usuario.formatoFecha) + '\'' +
                '}';
    }

    public Receta(String nombreReceta_,
                  String tipoPlato_,
                  NivelDeDificultad nivelDeDificultad_,
                  LocalDate fechaCreacion_,
                  byte[] imagenReceta_,
                  Usuario creadorDeLaReceta_) {
        this.tipoPlato = tipoPlato_.toLowerCase();
        this.nombreReceta = nombreReceta_;
        this.creadorDeLaReceta = creadorDeLaReceta_;
        this.nivelDificultad = nivelDeDificultad_;
        this.numComentarios = this.numFavoritos = this.duracionTotal = this.numPasos = 0;
        this.fechaCreacion =fechaCreacion_;
        setImagenReceta(imagenReceta_);
    }
}
