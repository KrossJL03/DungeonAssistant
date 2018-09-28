package bot.Exception;

public class PlayerCharacterAlreadyLeftException extends RuntimeException implements EncounterException {
    public PlayerCharacterAlreadyLeftException() {
        super("You have already left. You can't leave again unless you `$return` first");
    }
}
