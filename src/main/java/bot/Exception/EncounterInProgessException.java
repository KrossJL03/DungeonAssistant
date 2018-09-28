package bot.Exception;

public class EncounterInProgessException extends RuntimeException implements EncounterException {
    public EncounterInProgessException() {
        super("Hold your Capra! This encounter is already in progress.");
    }
}
