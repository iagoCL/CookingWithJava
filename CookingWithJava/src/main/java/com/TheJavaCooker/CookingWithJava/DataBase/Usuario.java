package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique=true, nullable=false)
    private String nombreUsuario;
    @Column(unique=false, nullable=false)
    private String contrasena;
    @Column(unique=true, nullable=true)
    private String correoElectronico;

    public long getId() {
        return id;
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

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                '}';
    }

    protected Usuario() {
    }

    public Usuario(String nombreUsuario_, String contrasena_, String correoElectronico_) {
        nombreUsuario = nombreUsuario_;
        contrasena = contrasena_;
        correoElectronico = correoElectronico_;
    }

}

