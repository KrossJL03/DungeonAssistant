package bot.Hostile.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import bot.Exception.EncounterException;

public class NoHostileFoundException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public NoHostileFoundException(String speciesName) {
        super(
            String.format(
                "I'm not familiar with %s, could you describe them for me using the `%sinsertHostile` command?",
                speciesName,
                CommandListener.COMMAND_KEY
            )
        );
    }
}
