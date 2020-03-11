package bot.Battle.Pvp;

import bot.Battle.CombatExplorer;
import bot.Battle.InitiativeTrackerException;
import bot.Battle.InitiativeTrackerInterface;
import bot.CustomException;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

class InitiativeCycle implements InitiativeTrackerInterface
{
    private CombatExplorer           currentExplorer;
    private Iterator<CombatExplorer> cycle;

    /**
     * Constructor.
     *
     * @param explorers Explorers
     */
    @NotNull InitiativeCycle(@NotNull ArrayList<CombatExplorer> explorers)
    {
        cycle = Iterables.cycle(explorers).iterator();
        currentExplorer = cycle.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@NotNull CombatExplorer explorer) throws CustomException
    {
        throw new CustomException("Explorers cannot be added once initiative has started.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CombatExplorer getCurrentExplorer() throws InitiativeTrackerException
    {
        return currentExplorer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CombatExplorer getNextExplorer() throws InitiativeTrackerException
    {
        do {
            currentExplorer = cycle.next();
        } while (!currentExplorer.isActive());

        currentExplorer.resetActions(false);

        return currentExplorer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull CombatExplorer explorer)
    {
        if (explorer.isActive()) {
            explorer.leave();
        }
    }
}
