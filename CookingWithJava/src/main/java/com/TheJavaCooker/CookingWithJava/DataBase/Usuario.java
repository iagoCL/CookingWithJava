package com.TheJavaCooker.CookingWithJava.DataBase;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Usuario")
@Table(name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nombreUsuario"}, name = Usuario.constraintNombreUsuario),
                @UniqueConstraint(columnNames = {"correoElectronico"}, name = Usuario.constraintCorreoElectronico)
        }
)
public class Usuario {
    public static final String constraintNombreUsuario = "CONSTRAINT_NOMBRE_USUARIO_UNICO";
    public static final String constraintCorreoElectronico = "CONSTRAINT_CORREO_ELECTRONICO_UNICO";
    public static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd LLL yy");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String nombreUsuario;
    @Column(nullable = false)
    private String contrasena;
    @Column(nullable = false)
    private String correoElectronico;
    @Column(nullable = false)
    private String nombreApellidos;
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @OneToMany(
            mappedBy = "creadorDeLaReceta",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<Receta> recetasCreadas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "favoritos")
    private List<Receta> recetasFavoritas = new ArrayList<>();

    public long getId() {
        return id;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos_) {
        this.nombreApellidos = nombreApellidos_;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
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

    public void setNombreUsuario(String nombreUsuario_) {
        this.nombreUsuario = nombreUsuario_;
    }

    public void setContrasena(String contrasena_) {
        this.contrasena = contrasena_;
    }

    public void setCorreoElectronico(String correoElectronico_) {
        this.correoElectronico = correoElectronico_;
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
                Objects.equals(nombreUsuario, usuario.nombreUsuario) &&
                Objects.equals(contrasena, usuario.contrasena) &&
                Objects.equals(nombreApellidos, usuario.nombreApellidos) &&
                Objects.equals(fechaCreacion, usuario.fechaCreacion) &&
                Objects.equals(correoElectronico, usuario.correoElectronico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreUsuario, contrasena, correoElectronico, fechaCreacion);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", nombreApellidos='" + nombreApellidos + '\'' +
                ", fechaDeCreacion='" + fechaCreacion.format(formatoFecha) + '\'' +
                '}';
    }

    @Transactional
    public List<Receta> getRecetasCreadas() {
        return recetasCreadas;
    }

    @Transactional
    public int getNumRecetasFavoritas() {
        return recetasFavoritas.size();
    }

    @Transactional
    public List<Receta> getRecetasFavoritas() {
        return recetasFavoritas;
    }

    @Transactional
    public int getNumRecetasCreadas() {
        return recetasCreadas.size();
    }

    protected Usuario() {
    }

    public Usuario(String nombreUsuario_, String contrasena_, String correoElectronico_, String nombreApellidos_) {
        this.nombreUsuario = nombreUsuario_;
        this.contrasena = contrasena_;
        this.correoElectronico = correoElectronico_;
        this.nombreApellidos = nombreApellidos_;
        resetFechaCreacion();
    }

}

