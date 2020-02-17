package bot.Battle;

import org.jetbrains.annotations.NotNull;

public class JoinActionResult implements ActionResultInterface
{
    private EncounteredExplorerInterface encounteredExplorer;
    private boolean                      isRosterFull;

    /**
     * Constructor.
     *
     * @param encounteredExplorer Encountered explorer that joined
     * @param isRosterFull        Is roster full
     */
    JoinActionResult(EncounteredExplorerInterface encounteredExplorer, boolean isRosterFull)
    {
        this.encounteredExplorer = encounteredExplorer;
        this.isRosterFull = isRosterFull;
    }

    /**
     * Get explorer that joined
     *
     * @return EncounteredExplorerInterface
     */
    public @NotNull EncounteredExplorerInterface getExplorer()
    {
        return encounteredExplorer;
    }

    /**
     * Is the roster full
     *
     * @return boolean
     */
    public boolean isRosterFull()
    {
        return isRosterFull;
    }
}
