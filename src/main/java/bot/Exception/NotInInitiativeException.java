package bot.Exception;

public class NotInInitiativeException extends RuntimeException implements EncounterException {
    public NotInInitiativeException() {
        super("There is no turn order currently, so there is no current player to be skipped.");
    }
}
