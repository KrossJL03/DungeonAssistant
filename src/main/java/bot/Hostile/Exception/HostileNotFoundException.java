package bot.Hostile.Exception;

import bot.CustomException;
import bot.MyProperties;

public class HostileNotFoundException extends CustomException
{
    private HostileNotFoundException(String message) {
        super(message);
    }

    public static HostileNotFoundException createNotInDatabase(String speciesName) {
        return new HostileNotFoundException(
            String.format(
                "I'm not familiar with %s, could you describe them for me using the `%sinsertHostile` command?",
                speciesName,
                MyProperties.COMMAND_PREFIX
            )
        );
    }
}
