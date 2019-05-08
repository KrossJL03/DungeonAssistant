package bot.Encounter;

import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

class EncounteredCreatureNotFoundException extends RuntimeException implements EncounterExceptionInterface
{
    /**
     * EncounteredCreatureNotFoundException constructor
     *
     * @param message Message
     */
    private @NotNull EncounteredCreatureNotFoundException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "creature"
     *
     * @param name Creature name
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull EncounteredCreatureNotFoundException createForCreature(@NotNull String name)
    {
        return new EncounteredCreatureNotFoundException(
            String.format(
                "I couldn't find any creatures with the name '%s' in this battle...",
                name
            )
        );
    }

    /**
     * Factory method for "current player"
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull EncounteredCreatureNotFoundException createForCurrentPlayer()
    {
        return new EncounteredCreatureNotFoundException("I can't find the player who's turn it is...");
    }

    /**
     * Factory method for "explorer"
     *
     * @param player Owner
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull EncounteredCreatureNotFoundException createForExplorer(@NotNull Player player)
    {
        return new EncounteredCreatureNotFoundException(
            String.format("I could not find your character in this encounter %s", player.getName())
        );
    }

    /**
     * Factory method for "creature"
     *
     * @param name Explorer name
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull EncounteredCreatureNotFoundException createForExplorer(@NotNull String name)
    {
        return new EncounteredCreatureNotFoundException(
            String.format("I can't find %s, are you sure they are part of this encounter?", name)
        );
    }

    /**
     * Factory method for "hostile"
     *
     * @param name Hostile name
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull EncounteredCreatureNotFoundException createForHostile(@NotNull String name)
    {
        return new EncounteredCreatureNotFoundException(
            String.format("I don't see any hostiles named %s in this encounter", name)
        );
    }

    /**
     * Factory method for "next player"
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull EncounteredCreatureNotFoundException createForNextPlayer()
    {
        return new EncounteredCreatureNotFoundException(
            "It looks like there aren't anymore players that can do anything else this turn..."
        );
    }
}
