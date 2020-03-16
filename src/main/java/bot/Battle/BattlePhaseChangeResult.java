package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BattlePhaseChangeResult
{
    private BattleContext     battleContext;
    private BattlePhaseChange phaseChange;

    /**
     * Constructor.
     *
     * @param phaseChange   Phase change
     * @param battleContext Battle context
     */
    BattlePhaseChangeResult(@NotNull BattlePhaseChange phaseChange, @NotNull BattleContext battleContext)
    {
        this.battleContext = battleContext;
        this.phaseChange = phaseChange;
    }

    /**
     * Get creatures
     *
     * @return ArrayList<CombatCreature>
     */
    public @NotNull ArrayList<CombatCreature> getCreatures()
    {
        return new ArrayList<>(battleContext.getCreatures());
    }

    /**
     * Get current player count
     *
     * @return int
     */
    public int getCurrentPartySize()
    {
        return battleContext.getCurrentPartySize();
    }

    /**
     * Get battle context
     *
     * @return BattleContext
     */
    @NotNull BattleContext getBattleContext()
    {
        return battleContext;
    }

    /**
     * Get battle type
     */
    @NotNull String getBattleType()
    {
        return battleContext.getType();
    }

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPartySize()
    {
        return battleContext.getMaxPartySize();
    }

    /**
     * Get next phase
     */
    @NotNull BattlePhase getNextPhase()
    {
        return phaseChange.getNewPhase();
    }

    /**
     * Get previous phase
     */
    @NotNull BattlePhase getPreviousPhase()
    {
        return phaseChange.getOldPhase();
    }

    /**
     * Get tier
     */
    @NotNull Tier getTier()
    {
        return battleContext.getTier();
    }

    /**
     * Is battle always joinable
     */
    boolean isAlwaysJoinable()
    {
        return battleContext.isAlwaysJoinable();
    }
}
