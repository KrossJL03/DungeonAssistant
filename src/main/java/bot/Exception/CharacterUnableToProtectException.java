package bot.Exception;

public class CharacterUnableToProtectException extends RuntimeException implements EncounterException {
    public CharacterUnableToProtectException() {
        super("You've already used your `$protect` for this encounter");
    }
}
