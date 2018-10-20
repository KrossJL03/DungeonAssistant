package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class ItemRecipientException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    private ItemRecipientException(String message) {
        super(message);
    }

    public static ItemRecipientException createReviveLiving(String name, int currentHp, int maxHp) {
        return new ItemRecipientException(
            String.format("%s is currently alive at [%d/%d] hitpoints. They can't be revived.", name, currentHp, maxHp)
        );
    }
}
