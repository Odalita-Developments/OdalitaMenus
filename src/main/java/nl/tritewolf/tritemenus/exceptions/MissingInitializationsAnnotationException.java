package nl.tritewolf.tritemenus.exceptions;

import java.lang.annotation.Annotation;

public final class MissingInitializationsAnnotationException extends Exception {

    public MissingInitializationsAnnotationException(Class<? extends Annotation> annotation, String type) {
        super("Failed to register " + type + " to container because '" + annotation.getSimpleName() + "' annotation is not present.");
    }
}