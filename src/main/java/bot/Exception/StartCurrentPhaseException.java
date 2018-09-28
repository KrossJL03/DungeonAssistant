package bot.Exception;

public class StartCurrentPhaseException extends RuntimeException implements EncounterException {
    public StartCurrentPhaseException(String phase) {
        super(String.format("The %s turn is already in progress", phase));
    }
}
