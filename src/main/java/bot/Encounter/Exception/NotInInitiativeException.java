package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Encounter.Exception.EncounterException;

public class NotInInitiativeException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public NotInInitiativeException() {
        super("There is no turn order currently, so there is no current player to be skipped.");
    }
}
