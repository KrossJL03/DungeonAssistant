package bot.Exception;

public class NotYourTurnException extends RuntimeException implements EncounterException {
    public NotYourTurnException() {
        super("Hey! Wait your turn!");
    }
}
