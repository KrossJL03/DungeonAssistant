package bot.Encounter;

import bot.CustomExceptionInterface;
import bot.Player.Player;

class RosterException extends RuntimeException implements CustomExceptionInterface {

    private RosterException(String message) {
        super(message);
    }

    static RosterException createFullRoster(Player player){
        return new RosterException(
            String.format("Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention())
        );
    }
}
