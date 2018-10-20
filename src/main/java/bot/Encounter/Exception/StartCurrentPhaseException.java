package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Encounter.Exception.EncounterException;

public class StartCurrentPhaseException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    public StartCurrentPhaseException(String phase) {
        super(String.format("The %s turn is already in progress", phase));
    }
}
