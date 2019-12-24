package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.ImageType;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Imagedb;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.ImageRepository;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@CacheConfig(cacheNames = "imageCache")
public class ImageService {
    @Autowired
    ImageRepository imagedbRepository;

    private static final int userX = 500;
    private static final int userY = 430;
    private static final int recipeX = 577;
    private static final int recipeY = 576;

    @CacheEvict(value = "imageCache", allEntries = true)
    public Pair<DatabaseService.Errors, Imagedb> createImagedb(byte[] imgUser_, ImageType imageType) {
        try {
            switch (imageType) {
            case RECIPE:
                imgUser_ = transformToRecipeImg(imgUser_);
                break;
            case USER:
                imgUser_ = transformToUserImg(imgUser_);
                break;
            default:
                // imgUser_ = imgUser_;
                break;
            }
            Imagedb imagedb = new Imagedb(imgUser_, imageType);
            imagedbRepository.save(imagedb);
            return Pair.of(DatabaseService.Errors.WITHOUT_ERRORS, imagedb);
        } catch (Exception exception) {
            PersonalDebug.printMsg("Unknown Exception: " + exception.toString());
            return Pair.of(DatabaseService.Errors.INCORRECT_IMAGE, new Imagedb());
        }
    }

    @Cacheable(value = "imageCache")
    public Imagedb searchById(long id) {
        return imagedbRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "imageCache", allEntries = true)
    public void deleteAll() {
        imagedbRepository.deleteAll();
    }

    private static byte[] transformToUserImg(byte[] bytes) throws IOException {
        return transformImage(bytes, userX, userY);
    }

    private static byte[] transformToRecipeImg(byte[] bytes) throws IOException {
        return transformImage(bytes, recipeX, recipeY);
    }

    private static byte[] transformImage(byte[] bytes_, int sizeX_, int sizeY_) throws IOException {
        // Converts to a image and scales the image
        Image image = ImageIO.read(new ByteArrayInputStream(bytes_));
        image = image.getScaledInstance(sizeX_, sizeY_, Image.SCALE_DEFAULT);

        // Transform to a buffer image
        BufferedImage bufferedImage = new BufferedImage(sizeX_, sizeY_, BufferedImage.TYPE_INT_RGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        // Convert from a bufferImage to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        bytes_ = outputStream.toByteArray();
        return bytes_;
    }
}