package bot.Encounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PhaseChangeResult implements EncounterRosterDataInterface
{
    private ArrayList<EncounteredExplorerInterface> encounteredExplorers;
    private ArrayList<EncounteredHostileInterface>  encounteredHostile;
    private EncounterPhaseInterface                 nextPhase;
    private EncounterPhaseInterface                 previousPhase;
    private TierInterface                           tier;
    private int                                     currentPlayerCount;
    private int                                     maxPlayerCount;

    /**
     * PhaseChangeResult constructor
     *
     * @param nextPhase            Next phase
     * @param previousPhase        Previous phase
     * @param tier                 Tier
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostile   Encountered hostiles
     * @param maxPlayerCount       Max player count
     * @param currentPlayerCount   Current player count
     */
    PhaseChangeResult(
        EncounterPhaseInterface nextPhase,
        EncounterPhaseInterface previousPhase,
        TierInterface tier,
        ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        ArrayList<EncounteredHostileInterface> encounteredHostile,
        int maxPlayerCount,
        int currentPlayerCount
    )
    {
        this.currentPlayerCount = currentPlayerCount;
        this.encounteredExplorers = encounteredExplorers;
        this.encounteredHostile = encounteredHostile;
        this.maxPlayerCount = maxPlayerCount;
        this.nextPhase = nextPhase;
        this.previousPhase = previousPhase;
        this.tier = tier;
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
    public @NotNull ArrayList<EncounteredExplorerInterface> getExplorers()
    {
        return new ArrayList<>(encounteredExplorers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<EncounteredHostileInterface> getHostiles()
    {
        return new ArrayList<>(encounteredHostile);
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
