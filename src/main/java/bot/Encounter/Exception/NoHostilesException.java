package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class NoHostilesException extends RuntimeException implements CustomExceptionInterface {

    public NoHostilesException() {
        super(
            String.format(
                "Uh, wait. Who are we fighting again? Tell me using `%saddHostile [species]`.",
                CommandListener.COMMAND_KEY
            )
        );
    }

}