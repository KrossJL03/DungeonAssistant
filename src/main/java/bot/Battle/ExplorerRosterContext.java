package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExplorerRosterContext
{
    private int                       currentPartySize;
    private ArrayList<CombatExplorer> explorers;
    private int                       maxPartySize;
    private Tier                      tier;

    /**
     * Constructor.
     *
     * @param maxPartySize     Max party size
     * @param currentPartySize Current party size
     * @param tier             Tier
     * @param explorers        Explorers
     */
    ExplorerRosterContext(
        int maxPartySize,
        int currentPartySize,
        @NotNull Tier tier,
        @NotNull ArrayList<CombatExplorer> explorers
    )
    {
        this.currentPartySize = currentPartySize;
        this.explorers = explorers;
        this.maxPartySize = maxPartySize;
        this.tier = tier;
    }

    /**
     * Get tier
     *
     * @return Tier
     */
    public @NotNull Tier getTier()
    {
        return tier;
    }

    /**
     * Get current party size
     *
     * @return int
     */
    int getCurrentPartySize()
    {
        return currentPartySize;
    }

    /**
     * Get explorers
     *
     * @return ArrayList
     */
    @NotNull ArrayList<CombatExplorer> getExplorers()
    {
        return new ArrayList<>(explorers);
    }

    /**
     * Get max party size
     *
     * @return int
     */
    int getMaxPartySize()
    {
        return maxPartySize;
    }
}
