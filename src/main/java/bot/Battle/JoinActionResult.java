package bot.Battle;

import org.jetbrains.annotations.NotNull;

class JoinActionResult implements ActionResultInterface
{
    private CombatExplorer explorer;
    private boolean        isRosterFull;

    /**
     * Constructor.
     *
     * @param explorer     Explorer that joined
     * @param isRosterFull Is roster full
     */
    JoinActionResult(@NotNull CombatExplorer explorer, boolean isRosterFull)
    {
        this.explorer = explorer;
        this.isRosterFull = isRosterFull;
    }

    /**
     * Get explorer that joined
     *
     * @return EncounteredExplorer
     */
    @NotNull CombatExplorer getExplorer()
    {
        return explorer;
    }

    /**
     * Is the roster full
     *
     * @return boolean
     */
    boolean isRosterFull()
    {
        return isRosterFull;
    }
}
