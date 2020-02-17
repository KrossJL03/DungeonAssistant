package bot.Battle;

import bot.CustomException;
import bot.MyProperties;
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

    /**
     * Factory method for "final phase"
     *
     * @return BattlePhaseException
     */
    static @NotNull BattlePhaseException createFinalPhase()
    {
        return new BattlePhaseException(
            String.format(
                "This encounter is over. If you'd like to start a new one use the `%screate encounter` command",
                MyProperties.COMMAND_PREFIX
            )
        );
    }

    /**
     * Factory method for "not initiative phase"
     *
     * @return BattlePhaseException
     */
    static @NotNull BattlePhaseException createNotInitiativePhase()
    {
        return new BattlePhaseException("There is no initiative currently.");
    }

    /**
     * Factory method for "not join phase"
     *
     * @return BattlePhaseException
     */
    static @NotNull BattlePhaseException createNotJoinPhase(@NotNull Mention mention)
    {
        return new BattlePhaseException(
            String.format(
                "Sorry %s, looks like you missed your chance. It's too late to join this encounter.",
                mention.getValue()
            )
        );
    }

    /**
     * Factory method for "not started"
     *
     * @return BattlePhaseException
     */
    static @NotNull BattlePhaseException createNotStarted()
    {
        return new BattlePhaseException("Hold your Rudi! This encounter hasn't even started yet.");
    }
}
