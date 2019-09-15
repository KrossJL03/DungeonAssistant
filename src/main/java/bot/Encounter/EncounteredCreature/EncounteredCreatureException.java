package bot.Encounter.EncounteredCreature;

import bot.CustomExceptionInterface;
import org.jetbrains.annotations.NotNull;

public class EncounteredCreatureException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * Constructor.
     *
     * @param message Message
     */
    private @NotNull EncounteredCreatureException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method of "is slain"
     *
     * @param slainName  Name of slain creature
     * @param slayerName Slayer name
     *
     * @return HostileRosterException
     */
    static @NotNull EncounteredCreatureException createIsSlain(@NotNull String slainName, @NotNull String slayerName)
    {
        return new EncounteredCreatureException(String.format("%s was slain by %s", slainName, slayerName));
    }

    /**
     * Factory method for "loot when not slain"
     *
     * @param name Creature name
     *
     * @return EncounteredCreatureException
     */
    static @NotNull EncounteredCreatureException createLootWhenNotSlain(@NotNull String name)
    {
        return new EncounteredCreatureException(
            String.format("%s has not been slain, they cannot be looted!", name)
        );
    }

    /**
     * Factory method for "out of bounds stat"
     *
     * @param name     Creature name
     * @param statName Stat name
     * @param statMin  Minimum amount for the stat
     *
     * @return EncounteredCreatureException
     */
    static @NotNull EncounteredCreatureException createStatLessThanMin(
        @NotNull String name,
        @NotNull String statName,
        int statMin
    )
    {
        return new EncounteredCreatureException(
            String.format("%s's %s must be greated than %d!", name, statName, statMin)
        );
    }

    /**
     * Factory method for "out of bounds stat"
     *
     * @param name     Creature name
     * @param statName Stat name
     * @param statMin  Minimum amount for the stat
     * @param statMax  Maximum amount for the stat
     *
     * @return EncounteredCreatureException
     */
    static @NotNull EncounteredCreatureException createStatOutOfBounds(
        @NotNull String name,
        @NotNull String statName,
        int statMin,
        int statMax
    )
    {
        return new EncounteredCreatureException(
            String.format("%s's %s must be between %d and %d!", name, statName, statMin, statMax)
        );
    }
}
