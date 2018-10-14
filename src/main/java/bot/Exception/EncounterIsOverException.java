package bot.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class EncounterIsOverException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public EncounterIsOverException() {
        super(
            String.format(
                "This encounter is over. If you'd like to start a new one use the `%sdm create` command",
                CommandListener.COMMAND_KEY
            )
        );
    }
}
