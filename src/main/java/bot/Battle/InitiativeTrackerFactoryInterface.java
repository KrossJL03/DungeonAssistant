package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface InitiativeTrackerFactoryInterface
{
    /**
     * Create initiative tracker
     *
     * @return InitiativeTrackerInterface
     */
    @NotNull InitiativeTrackerInterface create(@NotNull ArrayList<CombatExplorer> explorers);

    /**
     * Create null initiative tracker
     *
     * @return InitiativeTrackerInterface
     */
    @NotNull InitiativeTrackerInterface createNull();
}
