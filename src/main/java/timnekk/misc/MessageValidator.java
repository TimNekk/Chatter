package timnekk.misc;

public final class MessageValidator {
    private MessageValidator() {
    }

    public static boolean isValid(String message) {
        return message != null && !message.isEmpty();
    }

    public static String prepare(String message) {
        return message.trim();
    }
}