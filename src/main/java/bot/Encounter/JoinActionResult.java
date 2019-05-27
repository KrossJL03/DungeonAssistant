package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public class JoinActionResult implements JoinActionResultInterface
{
    private EncounteredExplorerInterface encounteredExplorer;
    private boolean                      isRosterFull;

    /**
     * JoinActionResult constructor
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredExplorerInterface getExplorer()
    {
        return encounteredExplorer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRosterFull()
    {
        return isRosterFull;
    }
}
