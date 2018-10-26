package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class ItemRecipientException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    private ItemRecipientException(String message) {
        super(message);
    }

    public static ItemRecipientException createDamagePlayer(String name, String itemName) {
        return new ItemRecipientException(
            String.format("No attacking your teammates! Using a %s on %s will hurt them!", itemName, name)
        );
    }

    public static ItemRecipientException createHealMaxHealth(String name, int maxHp) {
        return new ItemRecipientException(
            String.format("%s is currently at max health %d hitpoints. They can't be healed.", name, maxHp)
        );
    }

    public static ItemRecipientException createReviveLiving(String name, int currentHp, int maxHp) {
        return new ItemRecipientException(
            String.format("%s is currently alive at [%d/%d] hitpoints. They can't be revived.", name, currentHp, maxHp)
        );
    }
}
