package bot.Exception;

import bot.CustomExceptionInterface;

public class EncounterInProgessException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    public EncounterInProgessException() {
        super("Hold your Capra! This encounter is already in progress.");
    }
}
