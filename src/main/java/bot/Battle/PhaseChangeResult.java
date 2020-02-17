package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PhaseChangeResult implements EncounterRosterDataInterface
{
    private ArrayList<CombatCreature> creatures;
    private int                       currentPlayerCount;
    private int                       maxPlayerCount;
    private EncounterPhaseInterface   nextPhase;
    private EncounterPhaseInterface   previousPhase;
    private TierInterface             tier;

    /**
     * Constructor.
     *
     * @param nextPhase          Next phase
     * @param previousPhase      Previous phase
     * @param creatures          Creatures in the encounter
     * @param tier               Tier
     * @param maxPlayerCount     Max player count
     * @param currentPlayerCount Current player count
     */
    PhaseChangeResult(
        EncounterPhaseInterface nextPhase,
        EncounterPhaseInterface previousPhase,
        ArrayList<CombatCreature> creatures,
        TierInterface tier,
        int maxPlayerCount,
        int currentPlayerCount
    )
    {
        this.creatures = creatures;
        this.currentPlayerCount = currentPlayerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.nextPhase = nextPhase;
        this.previousPhase = previousPhase;
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
     * @return Phase
     */
    public @NotNull EncounterPhaseInterface getNextPhase()
    {
        return nextPhase;
    }

    /**
     * Get previous phase
     *
     * @return Phase
     */
    public @NotNull EncounterPhaseInterface getPreviousPhase()
    {
        return previousPhase;
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
