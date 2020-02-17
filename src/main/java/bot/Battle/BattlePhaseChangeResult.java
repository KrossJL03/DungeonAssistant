package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BattlePhaseChangeResult implements EncounterRosterDataInterface
{
    private ArrayList<CombatCreature> creatures;
    private int                       currentPlayerCount;
    private int                       maxPlayerCount;
    private BattlePhaseChange         phaseChange;
    private TierInterface             tier;

    /**
     * Constructor.
     *
     * @param phaseChange        Phase change
     * @param creatures          Creatures in the encounter
     * @param tier               Tier
     * @param maxPlayerCount     Max player count
     * @param currentPlayerCount Current player count
     */
    BattlePhaseChangeResult(
        @NotNull BattlePhaseChange phaseChange,
        @NotNull ArrayList<CombatCreature> creatures,
        @NotNull TierInterface tier,
        int maxPlayerCount,
        int currentPlayerCount
    )
    {
        this.creatures = creatures;
        this.currentPlayerCount = currentPlayerCount;
        this.maxPlayerCount = maxPlayerCount;
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
    public int getCurrentPlayerCount()
    {
        return currentPlayerCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxPlayerCount()
    {
        return maxPlayerCount;
    }

    /**
     * Get next phase
     *
     * @return BattlePhase
     */
    public @NotNull BattlePhase getNextPhase()
    {
        return phaseChange.getNewPhase();
    }

    /**
     * Get previous phase
     *
     * @return BattlePhase
     */
    public @NotNull BattlePhase getPreviousPhase()
    {
        return phaseChange.getOldPhase();
    }

    /**
     * Get tier
     *
     * @return Phase
     */
    public @NotNull TierInterface getTier()
    {
        return tier;
    }
}
