package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Encounter.Logger.Mention;
import bot.Player.Player;

public class ExplorerRosterException extends RuntimeException implements CustomExceptionInterface {

    private ExplorerRosterException(String message) {
        super(message);
    }

    public static ExplorerRosterException createFullRoster(Player player){
        return new ExplorerRosterException(
            String.format(
                "Uh oh, looks like the dungeon is full. Sorry %s.",
                (new Mention(player.getUserId()).getValue())
            )
        );
    }

    public static ExplorerRosterException createMaxPlayerCountLessThanOne() {
        return new ExplorerRosterException("You can't have less than 1 player... that just doesn't work");
    }

    public static ExplorerRosterException createNewMaxPlayerCountGreaterThanCurrentPlayerCount(
        int newMaxPlayerCount,
        int presentPlayerCount
    ) {
        return new ExplorerRosterException(
            String.format(
                "It looks like we already have %d present players." +
                "If you want to lower the max player count to %d please remove some players first.",
                presentPlayerCount,
                newMaxPlayerCount
            )
        );
    }

}
