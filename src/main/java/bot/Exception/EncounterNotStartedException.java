package bot.Exception;

public class EncounterNotStartedException extends RuntimeException implements EncounterException {
    public EncounterNotStartedException() {
        super("Hold your Rudi! This encounter hasn't even started yet.");
    }
}