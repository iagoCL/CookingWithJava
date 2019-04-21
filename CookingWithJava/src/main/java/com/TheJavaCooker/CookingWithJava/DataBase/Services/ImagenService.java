package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.*;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.*;
import com.TheJavaCooker.CookingWithJava.DataBase.TipoDeImagen;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@CacheConfig(cacheNames = "imagenCache")
public class ImagenService {
    @Autowired
    ImagendbRepository imagendbRepository;

    private static final int userX = 500;
    private static final int userY = 430;
    private static final int recetaX = 577;
    private static final int recetaY = 576;

    @CacheEvict(value = "imagenCache", allEntries = false)
    public Pair<DatabaseService.Errores, Imagendb> crearImagenDB(byte[] imgUsuario_, TipoDeImagen tipoDeImagen) {
        try {
            switch (tipoDeImagen) {
                case RECETA:
                    imgUsuario_ = transformarAImagenDeReceta(imgUsuario_);
                    break;
                case USUARIO:
                    imgUsuario_ = transformarAImagenDeUsuario(imgUsuario_);
                    break;
                default:
                    //imgUsuario_ = imgUsuario_;
                    break;
            }
            Imagendb imagendb = new Imagendb(imgUsuario_, tipoDeImagen);
            imagendbRepository.save(imagendb);
            return Pair.of(DatabaseService.Errores.SIN_ERRORES, imagendb);
        } catch (Exception e) {
            PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
            return Pair.of(DatabaseService.Errores.IMAGEN_ERRONEA, new Imagendb());
        }
    }

    @Cacheable(value = "imagenCache")
    public Imagendb buscarPorId(long id) {
        return imagendbRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "imagenCache", allEntries = false)
    public void eliminarTodos() {
        imagendbRepository.deleteAll();
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

        //Convertir de bufferImage a un byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        bytes_ = outputStream.toByteArray();
        return bytes_;
    }
}
