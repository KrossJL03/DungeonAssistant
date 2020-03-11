package bot.Battle;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

public class BattlePhaseException extends CustomException
{
    /**
     * Constructor.
     *
     * @param message Message
     */
    private @NotNull BattlePhaseException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "start current phase"
     *
     * @return BattlePhaseException
     */
    public static @NotNull BattlePhaseException createStartCurrentPhase(@NotNull String phase)
    {
        return new BattlePhaseException(String.format("The %s turn is already in progress", phase));
    }
}
