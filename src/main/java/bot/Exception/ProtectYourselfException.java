package bot.Exception;

public class ProtectYourselfException extends RuntimeException implements EncounterException {
    public ProtectYourselfException() {
        super("You can't protect yourself.");
    }
}
