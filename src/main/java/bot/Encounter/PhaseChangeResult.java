package bot.Encounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PhaseChangeResult implements EncounterRosterDataInterface
{
    private ArrayList<EncounteredExplorerInterface> encounteredExplorers;
    private ArrayList<EncounteredHostileInterface>  encounteredHostile;
    private EncounterPhase                          nextPhase;
    private EncounterPhase                          previousPhase;
    private int                                     currentPlayerCount;
    private int                                     maxPlayerCount;

    /**
     * PhaseChangeResult constructor
     *
     * @param nextPhase            Next phase
     * @param previousPhase        Previous phase
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostile   Encountered hostiles
     * @param maxPlayerCount       Max player count
     * @param currentPlayerCount   Current player count
     */
    PhaseChangeResult(
        EncounterPhase nextPhase,
        EncounterPhase previousPhase,
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
     * @return EncounterPhase
     */
    public @NotNull EncounterPhase getNextPhase()
    {
        return nextPhase;
    }

    /**
     * Get previous phase
     *
     * @return EncounterPhase
     */
    public @NotNull EncounterPhase getPreviousPhase()
    {
        return previousPhase;
    }
}
