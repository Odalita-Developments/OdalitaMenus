package nl.tritewolf.tritemenus.exceptions;

public class MissingInitializationsAnnotationException extends Exception{

    public MissingInitializationsAnnotationException() {
        super("Failed to add menu to container because TriteMenu annotation is not present.");
    }
}
