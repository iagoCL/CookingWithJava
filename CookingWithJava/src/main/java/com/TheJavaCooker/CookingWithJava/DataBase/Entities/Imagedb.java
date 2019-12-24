package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import com.TheJavaCooker.CookingWithJava.DataBase.ImageType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "Imagedb")
@Table(name = "Imagedb")
public class Imagedb implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false, name = "image")
    private byte[] image;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "image_type")
    private ImageType image_type;

    public long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public ImageType getImageType() {
        return image_type;
    }

    public Imagedb(byte[] image_, ImageType imageType) {
        this.image_type = imageType;
        this.image = image_;
    }

    public Imagedb() {
    }
}