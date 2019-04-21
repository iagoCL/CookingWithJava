package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity(name = "Usuario")
@Table(name = "Usuario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombre_usuario"}, name = Usuario.constraintNombreUsuario),
                @UniqueConstraint(columnNames = {"correo_electronico"}, name = Usuario.constraintCorreoElectronico)
        }
)
public class Usuario implements Serializable {
    public static final String constraintNombreUsuario = "CONSTRAINT_NOMBRE_USUARIO_UNICO";
    public static final String constraintCorreoElectronico = "CONSTRAINT_CORREO_ELECTRONICO_UNICO";
    public static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd LLL yy");
    private static final String randomHash1 = "dsefsSfsvreh435gvQEFQabhsr";
    private static final String randomHash2 = "AadsfFWT2354T3GEsfda435";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, name = "nombre_usuario")
    private String nombre_usuario;
    @Column(nullable = false, name = "contrasena")
    private String contrasena;
    @Column(nullable = false, name = "correo_electronico")
    private String correo_electronico;
    @Column(nullable = false, name = "nombre_apellidos")
    private String nombre_apellidos;
    @Column(nullable = false, name = "fecha_creacion")
    private LocalDate fecha_creacion;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imagendb_id")
    private Imagendb imagendb_id;

    @JsonIgnore
    @OneToMany(
            mappedBy = "creador_de_la_receta",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<Receta> recetas_creadas = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "favoritos")
    private List<Receta> recetas_favoritas = new ArrayList<>();

    @Column(nullable = true, name = "num_comentarios_usuario")
    private int num_comentarios_usuario;

    public long getId() {
        return id;
    }

    public long getImagenUsuario() {
        return imagendb_id.getId();
    }

    public String getNombreApellidos() {
        return nombre_apellidos;
    }

    public void setNombreApellidos(String nombreApellidos_) {
        this.nombre_apellidos = nombreApellidos_;
    }

    public String getNombreUsuario() {
        return nombre_usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreoElectronico() {
        return correo_electronico;
    }

    public LocalDate getFechaCreacion() {
        return fecha_creacion;
    }

    public String getStringFechaCreacion() {
        return fecha_creacion.format(Usuario.formatoFecha);
    }

    public boolean compararContrasena(String contrasena_) {
        return this.contrasena.equals(obtenercontrasena(contrasena_));
    }

    public void resetFechaCreacion() {
        this.fecha_creacion = LocalDate.now();
    }

    public void setNombreUsuario(String nombre_usuario_) {
        this.nombre_usuario = nombre_usuario_;
    }

    public void setContrasena(String contrasena_) {
        this.contrasena =obtenercontrasena(contrasena_);
    }
    private String obtenercontrasena( String contrasena_)
    {
        return "c:"+(randomHash1 + contrasena_ + randomHash2).hashCode();
    }

    public void setCorreoElectronico(String correo_electronico_) {
        this.correo_electronico = correo_electronico_;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    public boolean completeEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id &&
                Objects.equals(nombre_usuario, usuario.nombre_usuario) &&
                Objects.equals(contrasena, usuario.contrasena) &&
                Objects.equals(nombre_apellidos, usuario.nombre_apellidos) &&
                Objects.equals(correo_electronico, usuario.correo_electronico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre_usuario='" + nombre_usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", correo_electronico='" + correo_electronico + '\'' +
                ", nombre_apellidos='" + nombre_apellidos + '\'' +
                ", fechaDeCreacion='" + fecha_creacion.format(formatoFecha) + '\'' +
                '}';
    }

    /*@Transactional
    public Set<Receta> getRecetasCreadas() {
        Hibernate.initialize(this.recetas_creadas);
        return recetas_creadas;
    }*/

    public int getNumComentariosUsuario() {
        return num_comentarios_usuario;
    }

    public void nuevoComentario() {
        ++num_comentarios_usuario;
    }

    public void eliminarComentario() {
        --num_comentarios_usuario;
    }

    /*@Transactional
    public int getNumRecetasFavoritas() {
        return recetas_favoritas.size();
    }

    @Transactional
    public List<Receta> getRecetasFavoritas() {
        Hibernate.initialize(this.recetas_favoritas);
        return recetas_favoritas;
    }

    @Transactional
    public int getNumRecetasCreadas() {
        return recetas_creadas.size();
    }*/

    public Usuario() {
    }

    public Usuario(String nombre_usuario_, String contrasena_,
                   String correo_electronico_,
                   String nombre_apellidos_,
                   Imagendb imagendb_id_) {
        this.nombre_usuario = nombre_usuario_;
        setContrasena(contrasena_);
        this.correo_electronico = correo_electronico_;
        this.nombre_apellidos = nombre_apellidos_;
        resetFechaCreacion();
        imagendb_id = imagendb_id_;
        this.num_comentarios_usuario = 0;
    }

    public Map<String,Object> toJSON(){
        Map<String,Object> map = new HashMap<>();
        map.put("nombre_usuario",nombre_usuario);
        map.put("correo_electronico",correo_electronico);
        map.put("nombre_apellidos",nombre_apellidos);
        return map;
    }

}

