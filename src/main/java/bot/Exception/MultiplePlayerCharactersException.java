package bot.Exception;

import bot.CustomExceptionInterface;
import bot.Player.Player;

public class MultiplePlayerCharactersException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    public MultiplePlayerCharactersException(Player player, String name) {
        super(
            String.format(
                "%s, you have already joined this encounter with %s. " +
                "If you'd like to switch please talk to the DungeonMaster",
                player.getAsMention(),
                name
            )
        );
    }
}
