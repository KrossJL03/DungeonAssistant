package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface HostileRosterInterface
{
    /**
     * Add EncounteredHostileInterface to the hostileRoster
     *
     * @param newHostile Hostile to add
     */
    void addHostile(@NotNull EncounteredHostileInterface newHostile);

    /**
     * Get active hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredHostileInterface> getActiveHostiles();

    /**
     * Get all hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredHostileInterface> getAllHostiles();

    /**
     * Get hostile by name
     *
     * @param name Name of hostile to find
     *
     * @return EncounteredHostileInterface
     *
     * @throws EncounteredCreatureNotFoundException If hostile with name not found
     */
    @NotNull EncounteredHostileInterface getHostile(@NotNull String name);

    /**
     * Has active hostiles
     *
     * @return boolean
     */
    boolean hasActiveHostiles();

    /**
     * Is this a null roster
     *
     * @return boolean
     */
    boolean isNull();

    /**
     * Remove hostile
     *
     * @param hostile Hostile to remove
     *
     * @throws EncounteredCreatureNotFoundException If hostile is not in roster
     */
    void remove(@NotNull EncounteredHostileInterface hostile) throws EncounteredCreatureNotFoundException;
}
