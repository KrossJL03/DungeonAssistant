package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import bot.Encounter.Logger.Mention;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCharacterPresentException extends RuntimeException implements CustomExceptionInterface {

    private PlayerCharacterPresentException(String message) {
        super(message);
    }

    public static PlayerCharacterPresentException createHasAleadyLeft(@NotNull Player player) {
        return new PlayerCharacterPresentException(
            String.format(
                "%s You have already left. You can't leave again unless you `%srejoin` first",
                (new Mention(player.getUserId())).getValue(),
                CommandListener.COMMAND_KEY
            )
        );
    }

    public static PlayerCharacterPresentException createCannotRejoinIfPresent(@NotNull Player player) {
        return new PlayerCharacterPresentException(
            String.format(
                "%s You are currently active in this encounter. There is not need to `%srejoin`.",
                (new Mention(player.getUserId())).getValue(),
                CommandListener.COMMAND_KEY
            )
        );
    }
}