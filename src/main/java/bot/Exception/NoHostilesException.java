package bot.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class NoHostilesException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public NoHostilesException() {
        super(
            String.format(
                "Uh, wait. Who are we fighting again? Tell me using `%saddHostile [species]`.",
                CommandListener.COMMAND_KEY
            )
        );
    }
}