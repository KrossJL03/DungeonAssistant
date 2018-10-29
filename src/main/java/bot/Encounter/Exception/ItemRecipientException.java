package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class ItemRecipientException extends RuntimeException implements CustomExceptionInterface {

    private ItemRecipientException(String message) {
        super(message);
    }

    public static ItemRecipientException createBoostHostile(String name) {
        return new ItemRecipientException(
            String.format("You can't boost %s's stats. They are a hostile. They don't have stats", name)
        );
    }

    public static ItemRecipientException createDamagePlayer(String name, String itemName) {
        return new ItemRecipientException(
            String.format("No attacking your teammates! Using a %s on %s will hurt them!", itemName, name)
        );
    }

    public static ItemRecipientException createHealMaxHealth(String name, int maxHp) {
        return new ItemRecipientException(
            String.format("%s is currently at max health [%d HP]. They can't be healed.", name, maxHp)
        );
    }

    public static ItemRecipientException createReviveLiving(String name, int currentHp, int maxHp) {
        return new ItemRecipientException(
            String.format("%s is currently alive [%d/%d HP]. They can't be revived.", name, currentHp, maxHp)
        );
    }
}
