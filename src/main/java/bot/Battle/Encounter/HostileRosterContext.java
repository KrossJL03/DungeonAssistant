package bot.Battle.Encounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class HostileRosterContext
{
    private ArrayList<EncounteredHostile> hostiles;

    /**
     * Constructor.
     *
     * @param hostiles Hostiles
     */
    HostileRosterContext(@NotNull ArrayList<EncounteredHostile> hostiles)
    {
        this.hostiles = hostiles;
    }

    /**
     * Get hostiles
     *
     * @return ArrayList
     */
    @NotNull ArrayList<EncounteredHostile> getHostiles()
    {
        return new ArrayList<>(hostiles);
    }
}
