package bot.Battle.HostileEncounter;

import bot.CustomException;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

public class EncounteredExplorerException extends CustomException
{
    /**
     * EncounteredExplorerException constructor
     *
     * @param message Message
     */
    private @NotNull EncounteredExplorerException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "kill is not slain"
     *
     * @param explorerName Explorer name
     * @param opponentName Opponent name
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createKillIsNotSlain(
        @NotNull String explorerName,
        @NotNull String opponentName
    )
    {
        return new EncounteredExplorerException(
            String.format("%s is not slain and cannot be added to %s's kills.", opponentName, explorerName)
        );
    }

    /**
     * Factory method for "not present for opponent"
     *
     * @param explorerName Explorer name
     * @param opponentName Opponent name
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createNotPresentForOpponent(
        @NotNull String explorerName,
        @NotNull String opponentName
    )
    {
        return new EncounteredExplorerException(
            String.format("%s is not present to fight %s right now.", explorerName, opponentName)
        );
    }

    /**
     * Factory method for "protect actionless explorer"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectActionlessExplorer(@NotNull String name)
    {
        return new EncounteredExplorerException(
            String.format("%s's turn has already passed. They can not be protected.", name)
        );
    }

    /**
     * Factory method for "protect already used"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectAlreadyUsed()
    {
        return new EncounteredExplorerException(
            String.format(
                "You've already used your `%sprotect` for this encounter",
                MyProperties.COMMAND_PREFIX
            )
        );
    }

    /**
     * Factory method for "protect slain explorer"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectSlainExplorer(@NotNull String name)
    {
        return new EncounteredExplorerException(
            String.format("%s has already been slain. They can not be protected.", name)
        );
    }

    /**
     * Factory method for "protect yourself"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectYourself()
    {
        return new EncounteredExplorerException("You can't protect yourself.");
    }
}
