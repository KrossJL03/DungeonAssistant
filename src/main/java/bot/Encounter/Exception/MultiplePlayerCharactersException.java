package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Encounter.Logger.Mention;
import bot.Player.Player;

public class MultiplePlayerCharactersException extends RuntimeException implements CustomExceptionInterface {

    public MultiplePlayerCharactersException(Player player, String name) {
        super(
            String.format(
                "%s, you have already joined this encounter with %s. " +
                "If you'd like to switch please talk to the DungeonMaster",
                (new Mention(player.getUserId())).getValue(),
                name
            )
        );
    }

}
