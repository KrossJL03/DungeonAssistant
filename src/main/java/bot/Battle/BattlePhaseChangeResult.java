package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BattlePhaseChangeResult implements EncounterRosterDataInterface
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CombatCreature> getCreatures()
    {
        return new ArrayList<>(creatures);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentPartySize()
    {
        return battleContext.getCurrentPartySize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxPartySize()
    {
        return battleContext.getMaxPartySize();
    }

    /**
     * Get tier
     */
    public @NotNull Tier getTier()
    {
        return tier;
    }

    /**
     * Get battle type
     */
    @NotNull String getBattleType()
    {
        return battleContext.getType();
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
     * Is batle always joinable
     */
    boolean isAlwaysJoinable()
    {
        return battleContext.isAlwaysJoinable();
    }
}
