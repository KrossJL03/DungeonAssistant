package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BattlePhaseChangeResult
{
    private BattleContext             battleContext;
    private ArrayList<CombatCreature> creatures;
    private BattlePhaseChange         phaseChange;
    private Tier                      tier;

    /**
     * Constructor.
     *
     * @param phaseChange   Phase change
     * @param battleContext Battle context
     * @param creatures     Creatures in the encounter
     * @param tier          Tier
     */
    BattlePhaseChangeResult(
        @NotNull BattlePhaseChange phaseChange,
        @NotNull BattleContext battleContext,
        @NotNull ArrayList<CombatCreature> creatures,
        @NotNull Tier tier
    )
    {
        this.battleContext = battleContext;
        this.creatures = creatures;
        this.phaseChange = phaseChange;
        this.tier = tier;
    }

    /**
     * Get creatures
     *
     * @return ArrayList<CombatCreature>
     */
    public @NotNull ArrayList<CombatCreature> getCreatures()
    {
        return new ArrayList<>(creatures);
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
        return tier;
    }

    /**
     * Is battle always joinable
     */
    boolean isAlwaysJoinable()
    {
        return battleContext.isAlwaysJoinable();
    }
}
