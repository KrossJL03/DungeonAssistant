package bot.Encounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

interface EncounterRosterDataInterface
{
    /**
     * Get current player count
     *
     * @return int
     */
    int getCurrentPlayerCount();

    /**
     * Get encountered explorers
     *
     * @return ArrayList<EncounteredExplorerInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getExplorers();

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPlayerCount();

    /**
     * Get encountered hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredHostileInterface> getHostiles();
}
