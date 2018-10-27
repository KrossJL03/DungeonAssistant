package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class NotInInitiativeException extends RuntimeException implements CustomExceptionInterface {

    public NotInInitiativeException() {
        super("There is no turn order currently, so there is no current player to be skipped.");
    }

}
