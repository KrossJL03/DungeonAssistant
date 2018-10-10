package bot.Hostile.Exception;

import bot.CustomExceptionInterface;
import bot.Exception.EncounterException;

public class NoHostileFoundException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public NoHostileFoundException(String speciesName) {
        super(
            String.format(
                "I'm not familiar with %s, could you describe them for me using the `$insertHostile` command?",
                speciesName
            )
        );
    }
}
