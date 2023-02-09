package nl.tritewolf.tritemenus.utils;

public final class CharacterUtils {

    private CharacterUtils() {
    }

    public static boolean isDigits(CharSequence charSequence) {
        if (charSequence == null || charSequence.isEmpty()) return false;

        for (int i = 0; i < charSequence.length(); i++) {
            if (!Character.isDigit(charSequence.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}