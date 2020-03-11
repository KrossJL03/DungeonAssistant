package bot.Battle.Encounter;

import bot.Battle.CombatExplorer;
import bot.Battle.InitiativeTrackerFactoryInterface;
import bot.Battle.InitiativeTrackerInterface;
import bot.Battle.NullInitiativeTracker;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class InitiativeQueueFactory implements InitiativeTrackerFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull InitiativeTrackerInterface create(@NotNull ArrayList<CombatExplorer> explorers)
    {
        return new InitiativeQueue(explorers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull InitiativeTrackerInterface createNull()
    {
        return new NullInitiativeTracker();
    }
}
