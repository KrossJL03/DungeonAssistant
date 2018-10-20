package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Player.Player;

public class DungeonException extends RuntimeException implements EncounterException, CustomExceptionInterface {

    private DungeonException(String message) {
        super(message);
    }

    public static DungeonException createEmptyDungeon() {
        return new DungeonException("Well uh... this is awkward. Is seems we don't have any players...");
    }

    public static DungeonException createFullDungeon(Player player){
        return new DungeonException(
            String.format("Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention())
        );
    }
}
