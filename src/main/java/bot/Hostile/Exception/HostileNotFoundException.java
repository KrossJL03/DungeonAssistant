package bot.Hostile.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class HostileNotFoundException extends RuntimeException implements CustomExceptionInterface {
    private HostileNotFoundException(String message) {
        super(message);
    }

    public static HostileNotFoundException createNotInDatabase(String speciesName) {
        return new HostileNotFoundException(
            String.format(
                "I'm not familiar with %s, could you describe them for me using the `%sinsertHostile` command?",
                speciesName,
                CommandListener.COMMAND_KEY
            )
        );
    }
}
