package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class StartCurrentPhaseException extends RuntimeException implements CustomExceptionInterface {

    public StartCurrentPhaseException(String phase) {
        super(String.format("The %s turn is already in progress", phase));
    }

}
