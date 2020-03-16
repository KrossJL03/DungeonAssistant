package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract public class BattleContext
{
    private ExplorerRosterContext explorerRosterContext;
    private boolean               isAlwaysJoinable;
    private String                type;

    /**
     * Constructor.
     *
     * @param type                  Type of battle
     * @param isAlwaysJoinable      Can players join the battle at any time
     * @param explorerRosterContext Explorer roster context
     */
    protected BattleContext(
        @NotNull String type,
        boolean isAlwaysJoinable,
        @NotNull ExplorerRosterContext explorerRosterContext
    )
    {
        this.isAlwaysJoinable = isAlwaysJoinable;
        this.explorerRosterContext = explorerRosterContext;
        this.type = type;
    }

    /**
     * Get creatures
     *
     * @return ArrayList
     */
    abstract public @NotNull ArrayList<CombatCreature> getCreatures();

    /**
     * Get current party size
     *
     * @return int
     */
    int getCurrentPartySize()
    {
        return explorerRosterContext.getCurrentPartySize();
    }

    /**
     * Get max party size
     *
     * @return int
     */
    int getMaxPartySize()
    {
        return explorerRosterContext.getMaxPartySize();
    }

    /**
     * Get tier
     *
     * @return Tier
     */
    @NotNull Tier getTier()
    {
        return explorerRosterContext.getTier();
    }

    /**
     * Get battle type
     *
     * @return String
     */
    @NotNull String getType()
    {
        return type;
    }

    /**
     * Can players join at any time
     *
     * @return boolean
     */
    boolean isAlwaysJoinable()
    {
        return isAlwaysJoinable;
    }

    /**
     * Get explorers
     *
     * @return ArrayList
     */
    final protected @NotNull ArrayList<CombatExplorer> getExplorers()
    {
        return explorerRosterContext.getExplorers();
    }
}
