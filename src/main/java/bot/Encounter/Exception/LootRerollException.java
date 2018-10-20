package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Encounter.Exception.EncounterException;

public class LootRerollException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public LootRerollException() {
        super("Your loot has already been rolled");
    }
}
