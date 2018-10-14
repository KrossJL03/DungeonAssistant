package bot.Exception;

import bot.CustomExceptionInterface;

public class LootRerollException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public LootRerollException() {
        super("Your loot has already been rolled");
    }
}
