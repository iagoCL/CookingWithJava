package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Entity(name = "Imagendb")
@Table(name = "Imagendb")
public class Imagendb {
    private static final int userX = 500;
    private static final int userY = 430;
    private static final int recetaX = 577;
    private static final int recetaY = 576;

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

    private static byte[] transformarAImagenDeUsuario(byte[] bytes) throws IOException {
        return transformarAImagen(bytes, userX, userY);
    }

    private static byte[] transformarAImagenDeReceta(byte[] bytes) throws IOException {
        return transformarAImagen(bytes, recetaX, recetaY);
    }

    private static byte[] transformarAImagen(byte[] bytes_, int sizeX_, int sizeY_) throws IOException {
        //Convertir bytes en una image y escalar
        Image image = ImageIO.read(new ByteArrayInputStream(bytes_));
        image = image.getScaledInstance(sizeX_, sizeY_, Image.SCALE_DEFAULT);

        //Convertir imagen en un bufferImage
        BufferedImage bufferedImage = new BufferedImage(sizeX_, sizeY_, BufferedImage.TYPE_INT_RGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        //Concertit de bufferImage a un byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        bytes_ = outputStream.toByteArray();
        return bytes_;
    }

    public Imagendb(byte[] imagen, TipoDeImagen tipo_de_imagen) throws IOException {
        this.tipo_de_imagen = tipo_de_imagen;
        switch (tipo_de_imagen)
        {
            case RECETA:
                this.imagen = transformarAImagenDeReceta(imagen);
                break;
            case USUARIO:
                this.imagen = transformarAImagenDeUsuario(imagen);
                break;
            default:
                this.imagen = imagen;
                break;
        }
    }
    public Imagendb(){}
}
