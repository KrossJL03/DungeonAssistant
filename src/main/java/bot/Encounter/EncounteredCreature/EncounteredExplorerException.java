package bot.Encounter.EncounteredCreature;

import bot.CommandListener;
import bot.Constant;
import bot.CustomExceptionInterface;
import bot.Encounter.EncounterExceptionInterface;
import bot.Encounter.Logger.Mention;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

public class EncounteredExplorerException extends RuntimeException implements EncounterExceptionInterface
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
     * Factory method for "invalid stat name"
     *
     * @param name Stat name
     *
     * @return EncounteredExplorerException
     */
    public static @NotNull EncounteredExplorerException invalidStatName(String name)
    {
        return new EncounteredExplorerException(String.format("%s is not the name of a stat", name));
    }

    /**
     * Factory method for "cannot rejoin if present"
     *
     * @param player Owner
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createCannotRejoinIfPresent(@NotNull Player player)
    {
        return new EncounteredExplorerException(
            String.format(
                "%s You are currently active in this encounter. There is not need to `%srejoin`.",
                (Mention.createForPlayer(player.getUserId())).getValue(),
                CommandListener.COMMAND_KEY
            )
        );
    }

    /**
     * Factory method for "failed to add kill"
     *
     * @param explorerName Explorer name
     * @param killName     Kill name
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createFailedToAddKill(
        @NotNull String explorerName,
        @NotNull String killName
    )
    {
        return new EncounteredExplorerException(
            String.format("%s  was not present for %s's death", explorerName, killName)
        );
    }

    /**
     * Factory method for "has already left"
     *
     * @param player Owner
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createHasAleadyLeft(@NotNull Player player)
    {
        return new EncounteredExplorerException(
            String.format(
                "%s You have already left. You can't leave again unless you `%srejoin` first",
                (Mention.createForPlayer(player.getUserId())).getValue(),
                CommandListener.COMMAND_KEY
            )
        );
    }

    /**
     * Factory method for "has no actions"
     *
     * @param name Name fo explorer with no actions
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createHasNoActions(@NotNull String name)
    {
        return new EncounteredExplorerException(String.format("Looks like %s doesn't have any actions left", name));
    }

    /**
     * Factory method for "protect actionless explorer"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectActionlessExplorer(@NotNull String name) {
        return new EncounteredExplorerException(
            String.format("%s's turn has already passed. They can not be protected.", name)
        );
    }

    /**
     * Factory method for "protect already used"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectAlreadyUsed() {
        return new EncounteredExplorerException(
            String.format(
                "You've already used your `%sprotect` for this encounter",
                CommandListener.COMMAND_KEY
            )
        );
    }

    /**
     * Factory method for "protect slain explorer"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectSlainExplorer(@NotNull String name) {
        return new EncounteredExplorerException(
            String.format("%s has already been slain. They can not be protected.", name)
        );
    }

    /**
     * Factory method for "protect yourself"
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createProtectYourself() {
        return new EncounteredExplorerException("You can't protect yourself.");
    }

    /**
     * Factory method for "out of bounds stat"
     *
     * @param name     Explorer name
     * @param statName Stat name
     *
     * @return EncounteredExplorerException
     */
    static @NotNull EncounteredExplorerException createStatOutOfBounds(String name, String statName)
    {
        return new EncounteredExplorerException(
            String.format(
                "%s's %s must be between %d and %d!",
                name,
                statName,
                Constant.getStatMin(statName),
                Constant.getStatMax(statName)
            )
        );
    }
}
