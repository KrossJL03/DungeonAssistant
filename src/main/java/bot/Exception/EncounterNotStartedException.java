package bot.Exception;

import bot.CustomExceptionInterface;

public class EncounterNotStartedException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    public EncounterNotStartedException() {
        super("Hold your Rudi! This encounter hasn't even started yet.");
    }
}