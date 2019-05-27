package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface JoinActionResultInterface extends ActionResultInterface
{
    /**
     * Get explorer that joined
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull EncounteredExplorerInterface getExplorer();

    /**
     * Is the roster full
     *
     * @return boolean
     */
    boolean isRosterFull();
}
