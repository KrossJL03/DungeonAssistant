package bot.Exception;

import bot.Player.Player;

public class NoCharacterInEncounterException extends RuntimeException implements EncounterException{

    public NoCharacterInEncounterException(Player player) {
        super(String.format("I could not find your character in this encounter %s", player.getName()));
    }

    public NoCharacterInEncounterException(String name) {
        super(String.format("I can't find %s, are you sure they are part of this encounter?", name));
    }
}