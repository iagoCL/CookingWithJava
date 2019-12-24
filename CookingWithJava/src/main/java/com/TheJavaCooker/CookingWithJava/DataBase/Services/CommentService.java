package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Comment;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.User;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.CommentRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.RecipeRepository;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@CacheConfig(cacheNames = { "recipesCache", "usersCache" })
public class CommentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private CommentRepository commentRepository;

    @CacheEvict(allEntries = true)
    @Transactional
    public Pair<DatabaseService.Errors, Comment> createComment(String commentDescription_, String commentTitle_,
            Recipe recipeId_, User userId_) {
        return createCommentWithDate(commentDescription_, commentTitle_, LocalDateTime.now(), recipeId_, userId_);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public Pair<DatabaseService.Errors, Comment> createCommentWithDate(String commentDescription_, String commentTitle_,
            LocalDateTime commentDate_, Recipe recipeId_, User userId_) {
        Comment comment = new Comment(commentDescription_, commentTitle_, commentDate_, recipeId_, userId_);
        if (commentDescription_.isEmpty()) {
            PersonalDebug.printMsg("Null comment description: " + commentDescription_);
            return Pair.of(DatabaseService.Errors.NULL_COMMENT_DESCRIPTION, comment);
        } else if (commentTitle_.isEmpty()) {
            PersonalDebug.printMsg("Null comment title: " + commentDescription_);
            return Pair.of(DatabaseService.Errors.NULL_COMMENT_TITLE, comment);
        } else if (recipeId_ == null) {
            PersonalDebug.printMsg("Null recipe");
            return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, comment);
        } else if (userId_ == null) {
            PersonalDebug.printMsg("Null user");
            return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, comment);
        } else {
            if (commentDate_ == null) {
                PersonalDebug.printMsg("WARNING: Null date, using actual date");
                comment.resetCommentDate();
            }
            try {
                commentRepository.save(comment);
            } catch (org.springframework.dao.DataIntegrityViolationException exception) {
                if (exception.toString().contains(Comment.uniqueCommentConstraint)) {
                    PersonalDebug.printMsg("Repeated comment: " + exception.toString());
                    return Pair.of(DatabaseService.Errors.REPEATED_COMMENT, comment);
                } else {
                    PersonalDebug.printMsg(UserService.unknownConstraint + exception.toString());
                    return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, comment);
                }
            } catch (Exception exception) {
                PersonalDebug.printMsg(UserService.unknownException + exception.toString());
                return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, comment);
            }
            recipeId_.newComment();
            userId_.newComment();
            recipeRepository.save(recipeId_);
            userRepository.save(userId_);
            return Pair.of(DatabaseService.Errors.WITHOUT_ERRORS, comment);
        }
    }

    @CacheEvict(allEntries = true)
    public void deleteAll() {
        commentRepository.deleteAll();
    }
}