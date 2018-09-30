package bot.Exception;

import bot.Player.Player;

public class FullDungeonException extends RuntimeException implements EncounterException {
    public FullDungeonException(Player player){
        super(String.format("Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention()));
    }
}
