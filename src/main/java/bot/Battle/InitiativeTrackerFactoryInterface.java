package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

interface InitiativeTrackerFactoryInterface
{
    /**
     * Create initiative tracker
     *
     * @return InitiativeTrackerInterface
     */
    @NotNull InitiativeTrackerInterface create(@NotNull ArrayList<EncounteredExplorerInterface> explorers);

    /**
     * Create null initiative tracker
     *
     * @return InitiativeTrackerInterface
     */
    @NotNull InitiativeTrackerInterface createNull();
}
