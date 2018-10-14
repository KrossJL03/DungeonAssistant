package bot.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class MaxZeroPlayersException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public MaxZeroPlayersException() {
        super(
            String.format(
                "Wait! I don't know how many players to have! Tell me using `%sdm maxPlayers`.",
                CommandListener.COMMAND_KEY
            )
        );
    }
}
