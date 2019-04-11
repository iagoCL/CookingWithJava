package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.TheJavaCooker.CookingWithJava.DataBase.NivelDeDificultad;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.annotations.QueryEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SortNatural;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity(name = "Receta")
@QueryEntity
@Table(name = "Receta",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombre_receta"}, name = Receta.constraintNombreReceta)
        }
)
public class Receta implements Serializable {
    public static final String constraintNombreReceta = "CONSTRAINT_NOMBRE_RECETA_UNICA";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "tipo_plato")
    private String tipo_plato;
    @Column(nullable = false, name = "nombre_receta")
    private String nombre_receta;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "nivel_de_dificultad")
    private NivelDeDificultad nivel_de_dificultad;
    @Column(nullable = false, name = "fecha_creacion")
    private LocalDate fecha_creacion;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imagendb_id")
    private Imagendb imagendb_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario creador_de_la_receta;

    @OneToMany(
            mappedBy = "receta_id",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Ingrediente> ingredientes = new HashSet<>();

    @SortNatural
    @OneToMany(
            mappedBy = "receta_id",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Paso> pasos = new TreeSet<>();

    @OneToMany(
            mappedBy = "receta_id",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Utensilio> utensilios = new HashSet<>();

    @SortNatural
    @OneToMany(
            mappedBy = "receta_id",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Comentario> comentarios = new TreeSet<>();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST
            })
    @JoinTable(name = "usuario_recetas_favoritas",
            joinColumns = @JoinColumn(name = "receta_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> favoritos = new ArrayList<>();

    //@Formula(value = "select sum(p.duracion) from Paso p where p.receta_id = id")
    @Column(nullable = false, name = "duracion_total")
    private int duracion_total;
    @Column(nullable = false, name = "numero_pasos")
    private int numero_pasos;
    @Column(nullable = false, name = "numero_comentarios")
    private int numero_comentarios;
    @Column(nullable = false, name = "numero_favoritos")
    private int numero_favoritos;

    public Receta() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDuracionTotal() {
        return duracion_total;
    }

    public String getStringDuracionTotal() {
        return Paso.formatearTiempo(duracion_total);
    }

    public int getNumPasos() {
        return numero_pasos;
    }

    public void recalcNumPasos() {
        this.numero_pasos = pasos.size();
        int suma = 0;
        for (Paso paso : getPasos()) {
            suma += paso.getDuracion();
        }
        this.duracion_total = suma;
    }

    public int getNumComentarios() {
        return numero_comentarios;
    }

    public void nuevoComentario() {
        ++this.numero_comentarios;
    }

    public String getTipoPlato() {
        return tipo_plato;
    }

    public void setTipoPlato(String tipo_plato) {
        this.tipo_plato = tipo_plato.toLowerCase();
    }

    public String getNombreReceta() {
        return nombre_receta;
    }

    public void setNombreReceta(String nombre_receta) {
        this.nombre_receta = nombre_receta.toLowerCase();
    }

    public LocalDate getFechaCreacion() {
        return fecha_creacion;
    }

    public String getStringFechaCreacion() {
        return fecha_creacion.format(Usuario.formatoFecha);
    }

    public void resetFechaCreacion() {
        this.fecha_creacion = LocalDate.now();
    }

    public NivelDeDificultad getNivelDificultad() {
        return nivel_de_dificultad;
    }

    public void setNivelDificultad(NivelDeDificultad nivel_de_dificultad_) {
        this.nivel_de_dificultad = nivel_de_dificultad_;
    }

    @Transactional
    public Usuario getCreadorDeLaReceta() {
        Hibernate.initialize(this.creador_de_la_receta);
        return creador_de_la_receta;
    }

    public long getCreadorDeLaRecetaId() {
        return creador_de_la_receta.getId();
    }

    public long getImagenReceta() {
        return imagendb_id.getId();
    }

    public boolean marcarFavorito(Usuario usuario_) {
        if (marcadaComoFavorita(usuario_)) {
            return false;
        } else {
            favoritos.add(usuario_);
            ++numero_favoritos;
            return true;
        }
    }

    public boolean quitarFavorito(Usuario usuario_) {
        if (marcadaComoFavorita(usuario_)) {
            favoritos.remove(usuario_);
            --numero_favoritos;
            return true;
        } else {
            return false;
        }
    }

    public String mostrarMultilinea() {
        String string = toString();
        string += "\nIngredientes de la receta: " + ingredientes.size();
        for (Ingrediente i : getIngredientes()) {
            string += "\n " + i;
        }
        PersonalDebug.imprimir("\n\nUtensilios de la receta: " + utensilios.size());
        for (Utensilio i : getUtensilios()) {
            string += "\n " + i;
        }
        string += "\n\nPasos de la receta: " + pasos.size();
        for (Paso i : getPasos()) {
            string += "\n " + i;
        }
        return string;
    }

    @Transactional
    public Set<Ingrediente> getIngredientes() {
        Hibernate.initialize(this.ingredientes);
        return ingredientes;
    }

    @Transactional
    public Set<Paso> getPasos() {
        Hibernate.initialize(this.pasos);
        return pasos;
    }

    @Transactional
    public Set<Utensilio> getUtensilios() {
        Hibernate.initialize(this.utensilios);
        return utensilios;
    }

    @Transactional
    public Set<Comentario> getComentarios() {
        Hibernate.initialize(this.comentarios);
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
                Objects.equals(tipo_plato, receta.tipo_plato) &&
                Objects.equals(nombre_receta, receta.nombre_receta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", tipo_plato='" + tipo_plato + '\'' +
                ", nombre_receta='" + nombre_receta + '\'' +
                ", creador_de_la_receta=" + creador_de_la_receta.getId() +
                ", duracion_total=" + getStringDuracionTotal() +
                ", numero_pasos=" + numero_pasos +
                ", numero_favoritos=" + numero_favoritos +
                ", nivel_de_dificultad=" + nivel_de_dificultad.toString() +
                ", numero_comentarios=" + numero_comentarios +
                ", fechaDeCreacion='" + fecha_creacion.format(Usuario.formatoFecha) + '\'' +
                '}';
    }

    public Receta(String nombre_receta_,
                  String tipo_plato_,
                  NivelDeDificultad nivel_de_dificultad_,
                  LocalDate fecha_creacion_,
                  Imagendb imagen_receta_,
                  Usuario creador_de_la_receta_) {
        this.tipo_plato = tipo_plato_.toLowerCase();
        this.nombre_receta = nombre_receta_;
        this.creador_de_la_receta = creador_de_la_receta_;
        this.nivel_de_dificultad = nivel_de_dificultad_;
        this.numero_comentarios = this.numero_favoritos = this.duracion_total = this.numero_pasos = 0;
        this.fecha_creacion = fecha_creacion_;
        imagendb_id = imagen_receta_;
    }

    public Map<String,Object> toJSON(){
        Map<String,Object> map = new HashMap<>();
        map.put("tipo_plato",tipo_plato);
        map.put("nombre_receta",nombre_receta);
        map.put("nivel_de_dificultad",nivel_de_dificultad.toString());
        map.put("numero_comentarios",numero_comentarios);
        map.put("numero_favoritos",numero_favoritos);
        map.put("duracion_total",getStringDuracionTotal());

        map.put("creador",getCreadorDeLaReceta().toJSON());

        map.put("numero_pasos",numero_pasos);
        Map<String,Object> mapAux = new HashMap<>();
        for(Paso paso : getPasos())
        {
            mapAux.put("paso-"+paso.getNumPaso(),paso.toJSON());
        }
        map.put("pasos",mapAux);

        mapAux = new HashMap<>();
        int i = 0;
        for(Ingrediente ingrediente : getIngredientes())
        {
            ++i;
            mapAux.put("ingrediente-"+i,ingrediente.toJSON());
        }
        map.put("numero_ingredientes",i);
        map.put("ingredientes",mapAux);

        mapAux = new HashMap<>();
        i = 0;
        for(Utensilio utensilio : getUtensilios())
        {
            ++i;
            mapAux.put("utensilio-"+i,utensilio.toJSON());
        }
        map.put("numero_utensilios",i);
        map.put("utensilios",mapAux);
        return map;
    }
}
