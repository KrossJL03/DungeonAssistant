package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Player.Player;

public class EncounterDataNotFoundException extends RuntimeException implements CustomExceptionInterface {

    private EncounterDataNotFoundException(String message) {
        super(message);
    }

    public static EncounterDataNotFoundException createForEncounterData(String name) {
        return new EncounterDataNotFoundException(
            String.format(
                "I couldn't find any creatures with the name '%s' in this battle...",
                name
            )
        );
    }

    public static EncounterDataNotFoundException createForHostile(String name) {
        return new EncounterDataNotFoundException(
            String.format("I don't see any hostiles named %s in this encounter", name)
        );
    }

    public static EncounterDataNotFoundException createForCurrentPlayer() {
        return new EncounterDataNotFoundException("I can't find the player who's turn it is...");
    }

    public static EncounterDataNotFoundException createForNextPlayer() {
        return new EncounterDataNotFoundException(
            "It looks like there aren't anymore players that can do anything else this turn..."
        );
    }

    public static EncounterDataNotFoundException createForPlayerCharacter(Player player) {
        return new EncounterDataNotFoundException(
            String.format("I could not find your character in this encounter %s", player.getName())
        );
    }

    public static EncounterDataNotFoundException createForPlayerCharacter(String name) {
        return new EncounterDataNotFoundException(
            String.format("I can't find %s, are you sure they are part of this encounter?", name)
        );
    }

    public static EncounterDataNotFoundException createForRecipient(String name) {
        return new EncounterDataNotFoundException(
            String.format(
                "I couldn't find anyone named '%s' in this battle... Are you sure it's spelled correctly?",
                name
            )
        );
    }
}
