package com.TheJavaCooker.CookingWithJava.DataBase.Services;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Step;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.StepRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StepService {
    @Autowired
    private StepRepository stepRepository;

    static DatabaseService.Errors checkSteps(List<Pair<Integer, String>> stepsList) {
        for (int i = 0, l = stepsList.size(); i < l; ++i) {
            Pair<Integer, String> step = stepsList.get(i);
            if (step.getSecond().isEmpty()) {
                PersonalDebug.printMsg("Null description of step:" + i);
                return DatabaseService.Errors.NULL_STEP_DESCRIPTION;
            } else if (step.getFirst() < 0) {
                PersonalDebug.printMsg("Null or negative time: " + step.getFirst());
                return DatabaseService.Errors.INCORRECT_STEP_TIME;
            }
        }
        return DatabaseService.Errors.WITHOUT_ERRORS;
    }

    Pair<DatabaseService.Errors, Step> createStep(int stepNumber, int time, String stepDescription, Recipe recipe_) {
        Step step = new Step(stepNumber, time, stepDescription, recipe_);
        if (stepNumber < 1) {
            PersonalDebug.printMsg("Null step number: " + stepNumber);
            return Pair.of(DatabaseService.Errors.INCORRECT_STEP_NUMBER, step);
        } else if (time < 1) {
            PersonalDebug.printMsg("Null or negative time step: " + time);
            return Pair.of(DatabaseService.Errors.INCORRECT_STEP_TIME, step);
        } else if (stepDescription.isEmpty()) {
            PersonalDebug.printMsg("Null step description: " + stepDescription);
            return Pair.of(DatabaseService.Errors.NULL_STEP_DESCRIPTION, step);
        } else if (recipe_ == null) {
            PersonalDebug.printMsg("Null Recipe");
            return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, step);
        } else {
            try {
                stepRepository.save(step);
            } catch (org.springframework.dao.DataIntegrityViolationException exception) {
                if (exception.toString().contains(Step.constraintStepName)) {
                    PersonalDebug.printMsg("Repeated step number: " + exception.toString());
                    return Pair.of(DatabaseService.Errors.REPEATED_STEP_NUMBER, step);
                } else {
                    PersonalDebug.printMsg(UserService.unknownConstraint + exception.toString());
                    return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, step);
                }

            } catch (Exception exception) {
                PersonalDebug.printMsg(UserService.unknownException + exception.toString());
                return Pair.of(DatabaseService.Errors.UNKNOWN_ERROR, step);
            }
            recipe_.getSteps().add(step);
            return Pair.of(DatabaseService.Errors.WITHOUT_ERRORS, step);
        }
    }

    void deleteAll() {
        stepRepository.deleteAll();
    }
}