package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Player.Player;

public class PCRosterException extends RuntimeException implements CustomExceptionInterface {

    private PCRosterException(String message) {
        super(message);
    }

    public static PCRosterException createFullRoster(Player player){
        return new PCRosterException(
            String.format("Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention())
        );
    }

    public static PCRosterException createMaxPlayerCountLessThanOne() {
        return new PCRosterException("You can't have less than 1 player... that just doesn't work");
    }

    public static PCRosterException createNewMaxPlayerCountGreaterThanCurrentPlayerCount(
        int newMaxPlayerCount,
        int presentPlayerCount
    ) {
        return new PCRosterException(
            String.format(
                "It looks like we already have %d present players." +
                "If you want to lower the max player count to %d please remove some players first.",
                presentPlayerCount,
                newMaxPlayerCount
            )
        );
    }

}
