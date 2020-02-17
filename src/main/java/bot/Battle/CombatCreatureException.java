package bot.Battle;

import bot.Battle.Logger.Mention;
import bot.CustomException;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

public class CombatCreatureException extends CustomException
{
    /**
     * Constructor.
     *
     * @param message Message
     */
    private @NotNull CombatCreatureException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "loot when not slain"
     *
     * @param name Creature name
     *
     * @return CombatCreatureException
     */
    public static @NotNull CombatCreatureException createLootWhenNotSlain(@NotNull String name)
    {
        return new CombatCreatureException(
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
     * @return CombatCreatureException
     */
    public static @NotNull CombatCreatureException createStatLessThanMin(
        @NotNull String name,
        @NotNull String statName,
        int statMin
    )
    {
        return new CombatCreatureException(
            String.format("%s's %s must be greated than %d!", name, statName, statMin)
        );
    }

    /**
     * Factory method for "cannot rejoin if present"
     *
     * @param player Owner
     *
     * @return CombatCreatureException
     */
    static @NotNull CombatCreatureException createCannotRejoinIfPresent(@NotNull Player player)
    {
        return new CombatCreatureException(String.format(
            "%s You are currently active in this encounter.",
            (Mention.createForPlayer(player.getUserId())).getValue()
        ));
    }

    /**
     * Factory method for "has already left"
     *
     * @param player Owner
     *
     * @return EncounteredExplorerException
     */
    static @NotNull CombatCreatureException createHasAlreadyLeft(@NotNull Player player)
    {
        return new CombatCreatureException(String.format(
            "%s You have already left.",
            (Mention.createForPlayer(player.getUserId())).getValue()
        ));
    }

    /**
     * Factory method for "has no actions"
     *
     * @param name Name fo explorer with no actions
     *
     * @return CombatCreatureException
     */
    static @NotNull CombatCreatureException createHasNoActions(@NotNull String name)
    {
        return new CombatCreatureException(String.format("Looks like %s doesn't have any actions left", name));
    }

    /**
     * Factory method for "invalid stat name"
     *
     * @param name         Stat name
     * @param creatureType Creature type
     *
     * @return CombatCreatureException
     */
    public static @NotNull CombatCreatureException createInvalidStatName(
        @NotNull String name,
        @NotNull String creatureType
    )
    {
        return new CombatCreatureException(String.format("%s is a valid %s stat", name, creatureType));
    }

    /**
     * Factory method of "is slain"
     *
     * @param slainName  Name of slain creature
     * @param slayerName Slayer name
     *
     * @return HostileRosterException
     */
    static @NotNull CombatCreatureException createIsSlain(@NotNull String slainName, @NotNull String slayerName)
    {
        return new CombatCreatureException(String.format("%s was slain by %s", slainName, slayerName));
    }

    /**
     * Factory method for "out of bounds stat"
     *
     * @param name     Creature name
     * @param statName Stat name
     * @param statMin  Minimum amount for the stat
     * @param statMax  Maximum amount for the stat
     *
     * @return CombatCreatureException
     */
    static @NotNull CombatCreatureException createStatOutOfBounds(
        @NotNull String name,
        @NotNull String statName,
        int statMin,
        int statMax
    )
    {
        return new CombatCreatureException(
            String.format("%s's %s must be between %d and %d!", name, statName, statMin, statMax)
        );
    }
}
