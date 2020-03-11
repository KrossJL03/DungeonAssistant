package bot.Battle;

import bot.CustomException;
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
}
