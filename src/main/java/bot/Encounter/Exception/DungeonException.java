package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Player.Player;

public class DungeonException extends RuntimeException implements CustomExceptionInterface {

    private DungeonException(String message) {
        super(message);
    }

    public static DungeonException createNoPlayersHaveJoined() {
        return new DungeonException("Wait, we can't start yet! No players have joined!");
    }

    public static DungeonException createFullDungeon(Player player){
        return new DungeonException(
            String.format("Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention())
        );
    }
}
