package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

interface EncounterRosterDataInterface
{
    /**
     * Get creatures
     *
     * @return ArrayList<CombatCreature>
     */
    @NotNull ArrayList<CombatCreature> getCreatures();

    /**
     * Get current player count
     *
     * @return int
     */
    int getCurrentPartySize();

    /**
     * Get max player count
     *
     * @return int
     */
    int getMaxPartySize();
}
