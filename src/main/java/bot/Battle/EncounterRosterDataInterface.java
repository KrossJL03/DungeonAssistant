package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

interface EncounterRosterDataInterface
{
    /**
     * Get creatures
     *
     * @return ArrayList<EncounteredCreatureInterface>
     */
    @NotNull ArrayList<EncounteredCreatureInterface> getCreatures();

    /**
     * Get current player count
     *
     * @return int
     */
    int getCurrentPlayerCount();

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPlayerCount();
}