package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class ItemException extends RuntimeException implements CustomExceptionInterface {

    private ItemException(String message) {
        super(message);
    }

    public static ItemException createInvalidParameter(String parameter) {
        return new ItemException(String.format("I don't know what %s is. This name means nothing to me.", parameter));
    }

    public static ItemException createMissingStatName(String itemName) {
        return new ItemException(String.format("%s affects stats. Please provide a stat use it on.", itemName));
    }

    public static ItemException createMultipleRecipients(String name1, String name2) {
        return new ItemException(String.format("You can't use an item on both %s and %s", name1, name2));
    }

    public static ItemException createMultipleStats(String name1, String name2) {
        return new ItemException(String.format("You can't use an item on two stats %s and %s", name1, name2));
    }
}
