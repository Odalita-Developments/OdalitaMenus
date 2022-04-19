package nl.tritewolf.tritemenus.exceptions;

public final class MissingInitializationsAnnotationException extends Exception {

    public MissingInitializationsAnnotationException() {
        super("Failed to register menu to container because TriteMenu annotation is not present.");
    }
}