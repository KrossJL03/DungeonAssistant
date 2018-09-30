package bot.Exception;

public class LootRerollException extends RuntimeException implements EncounterException {
    public LootRerollException() {
        super("Your loot has already been rolled");
    }
}
