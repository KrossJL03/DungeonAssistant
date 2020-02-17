package bot.Battle.HostileEncounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface HostileRosterInterface
{
    /**
     * Add hostile to the hostileRoster
     *
     * @param newHostile Hostile to add
     */
    void addHostile(@NotNull EncounteredHostile newHostile);

    /**
     * Get active hostiles
     *
     * @return ArrayList<EncounteredHostile>
     */
    @NotNull ArrayList<EncounteredHostile> getActiveHostiles();

    /**
     * Get all hostiles
     *
     * @return ArrayList<EncounteredHostile>
     */
    @NotNull ArrayList<EncounteredHostile> getAllHostiles();

    /**
     * Get hostile by name
     *
     * @param name Name of hostile to find
     *
     * @return EncounteredHostile
     *
     * @throws HostileRosterException If hostile with name not found
     */
    @NotNull EncounteredHostile getHostile(@NotNull String name) throws HostileRosterException;

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
     * @throws HostileRosterException If hostile is not in roster
     */
    void remove(@NotNull EncounteredHostile hostile) throws HostileRosterException;
}
