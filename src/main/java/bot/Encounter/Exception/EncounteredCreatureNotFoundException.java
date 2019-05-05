package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Player.Player;

public class EncounteredCreatureNotFoundException extends RuntimeException implements CustomExceptionInterface {

    private EncounteredCreatureNotFoundException(String message) {
        super(message);
    }

    public static EncounteredCreatureNotFoundException createForCreature(String name) {
        return new EncounteredCreatureNotFoundException(
            String.format(
                "I couldn't find any creatures with the name '%s' in this battle...",
                name
            )
        );
    }

    public static EncounteredCreatureNotFoundException createForHostile(String name) {
        return new EncounteredCreatureNotFoundException(
            String.format("I don't see any hostiles named %s in this encounter", name)
        );
    }

    public static EncounteredCreatureNotFoundException createForCurrentPlayer() {
        return new EncounteredCreatureNotFoundException("I can't find the player who's turn it is...");
    }

    public static EncounteredCreatureNotFoundException createForNextPlayer() {
        return new EncounteredCreatureNotFoundException(
            "It looks like there aren't anymore players that can do anything else this turn..."
        );
    }

    public static EncounteredCreatureNotFoundException createForExplorer(Player player) {
        return new EncounteredCreatureNotFoundException(
            String.format("I could not find your character in this encounter %s", player.getName())
        );
    }

    public static EncounteredCreatureNotFoundException createForExplorer(String name) {
        return new EncounteredCreatureNotFoundException(
            String.format("I can't find %s, are you sure they are part of this encounter?", name)
        );
    }

    public static EncounteredCreatureNotFoundException createForRecipient(String name) {
        return new EncounteredCreatureNotFoundException(
            String.format(
                "I couldn't find anyone named '%s' in this battle... Are you sure it's spelled correctly?",
                name
            )
        );
    }
}
