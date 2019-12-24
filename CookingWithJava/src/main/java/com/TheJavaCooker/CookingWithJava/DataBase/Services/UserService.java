package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.DataBase.ImageType;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Imagedb;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.UserRepository;
import com.TheJavaCooker.CookingWithJava.PersonalDebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@CacheConfig(cacheNames = "usersCache")
public class UserService {
    public static String unknownConstraint = "Unknown Constraint Violation Exception: ";
    public static String unknownException = "Unknown Exception: ";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    @CacheEvict(value = "usersCache", allEntries = true)
    public Pair<DatabaseService.Errors, User> createUser(String userName_, String pass_, String mail_, String name_,
            byte[] imgUser_) {
        Pair<DatabaseService.Errors, Imagedb> pair = imageService.createImagedb(imgUser_, ImageType.USER);
        if (pair.getFirst() == DatabaseService.Errors.WITHOUT_ERRORS) {
            return updateUser(new User(userName_, pass_, mail_, name_, pair.getSecond()));
        } else {
            return Pair.of(pair.getFirst(), new User());
        }
    }

    @CacheEvict(value = "usersCache", allEntries = true)
    public Pair<DatabaseService.Errors, User> updateUser(User user_) {
        if (user_.getUserName().isEmpty()) {
            PersonalDebug.printMsg("Null user name " + user_.getUserName());
            return Pair.of(DatabaseService.Errors.NULL_USER_NAME, user_);
        } else if (user_.getPass().isEmpty()) {
            PersonalDebug.printMsg("Null password: " + user_.getPass());
            return Pair.of(DatabaseService.Errors.NULL_PASSWORD, user_);
        } else if (user_.getUserCompleteName().isEmpty()) {
            PersonalDebug.printMsg("Null user complete name: " + user_.getUserCompleteName());
            return Pair.of(DatabaseService.Errors.NULL_NAME_SURNAME, user_);
        } else if (user_.getUserMail().isEmpty()) {
            PersonalDebug.printMsg("Null email: " + user_.getUserMail());
            return Pair.of(DatabaseService.Errors.NULL_MAIL, user_);
        } else {
            if (user_.getCreationDate() == null) {
                PersonalDebug.printMsg("WARNING: Null date, using actual date");
                user_.setCreationDate();
            }
            try {
                userRepository.save(user_);
            } catch (org.springframework.dao.DataIntegrityViolationException exception) {
                if (exception.toString().contains(User.constraintUserName)) {
                    PersonalDebug.printMsg("Repeated user name: " + exception.toString());
                    return Pair.of(DatabaseService.Errors.REPEATED_USER_NAME, user_);
                } else if (exception.toString().contains(User.constraintUserMail)) {
                    PersonalDebug.printMsg("Repeated user mail: " + exception.toString());
                    return Pair.of(DatabaseService.Errors.REPEATED_USER_MAIL, user_);
                } else {
                    PersonalDebug.printMsg(unknownConstraint + exception.toString());
                    return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, user_);
                }

            } catch (Exception exception) {
                PersonalDebug.printMsg(unknownException + exception.toString());
                return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, user_);
            }
            return Pair.of(DatabaseService.Errors.WITHOUT_ERRORS, user_);
        }
    }

    @Cacheable(value = "usersCache")
    public User searchById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "usersCache")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @CacheEvict(allEntries = true)
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Cacheable(value = "usersCache")
    public long getNumUsers() {
        return userRepository.count();
    }
}