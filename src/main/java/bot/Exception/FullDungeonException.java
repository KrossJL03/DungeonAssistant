package bot.Exception;

import net.dv8tion.jda.core.entities.User;

public class FullDungeonException extends RuntimeException implements EncounterException {
    public FullDungeonException(User player){
        super(String.format("Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention()));
    }
}
