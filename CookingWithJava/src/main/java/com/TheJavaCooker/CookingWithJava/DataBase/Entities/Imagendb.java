package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.TheJavaCooker.CookingWithJava.DataBase.TipoDeImagen;

import javax.persistence.*;

@Entity(name = "Imagendb")
@Table(name = "Imagendb")
public class Imagendb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false, name = "imagen")
    private byte[] imagen;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo_de_imagen")
    private TipoDeImagen tipo_de_imagen;

    public long getId() {
        return id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public TipoDeImagen getTipoDeImagen() {
        return tipo_de_imagen;
    }

    public Imagendb(byte[] imagen_, TipoDeImagen tipo_de_imagen) {
        this.tipo_de_imagen = tipo_de_imagen;
        this.imagen = imagen_;
    }

    public Imagendb() {
    }
}
