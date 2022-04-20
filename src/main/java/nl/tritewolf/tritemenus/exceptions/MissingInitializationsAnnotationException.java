package nl.tritewolf.tritemenus.exceptions;

import java.lang.annotation.Annotation;

public final class MissingInitializationsAnnotationException extends Exception {

    public MissingInitializationsAnnotationException(String message) {
        super(message);
    }

    public MissingInitializationsAnnotationException(Class<? extends Annotation> annotation, String type) {
        this("Failed to register " + type + " to container because '" + annotation.getSimpleName() + "' annotation is not present.");
    }
}