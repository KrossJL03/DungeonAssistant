package bot.Battle.Encounter;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class HostileRoster
{
    private static int                           BASE_CHAR = 65;
    private        ArrayList<EncounteredHostile> hostileRoster;

    /**
     * Constructor.
     */
    HostileRoster()
    {
        this.hostileRoster = new ArrayList<>();
    }

    /**
     * Add hostile to the hostileRoster
     *
     * @param newHostile Hostile to add
     */
    void addHostile(@NotNull EncounteredHostile newHostile)
    {
        if (newHostile.hasNickname()) {
            assertUniqueNickname(newHostile.getName());
        } else {
            handleSameSpecies(newHostile);
        }

        hostileRoster.add(newHostile);
    }

    /**
     * Get active hostiles
     *
     * @return ArrayList<EncounteredHostile>
     */
    @NotNull ArrayList<EncounteredHostile> getActiveHostiles()
    {
        ArrayList<EncounteredHostile> activeHostiles = new ArrayList<>();
        for (EncounteredHostile hostile : hostileRoster) {
            if (!hostile.isSlain()) {
                activeHostiles.add(hostile);
            }
        }

        return activeHostiles;
    }

    /**
     * Get all hostiles
     *
     * @return ArrayList<EncounteredHostile>
     */
    @NotNull ArrayList<EncounteredHostile> getAllHostiles()
    {
        return new ArrayList<>(hostileRoster);
    }

    /**
     * Get hostile by name
     *
     * @param name Name of hostile to find
     *
     * @return EncounteredHostile
     *
     * @throws CustomException If hostile with name not found
     */
    @NotNull EncounteredHostile getHostile(@NotNull String name) throws CustomException
    {
        for (EncounteredHostile hostile : hostileRoster) {
            if (hostile.isName(name)) {
                return hostile;
            }
        }

        throw new CustomException(String.format("I don't see any hostiles named %s in this encounter", name));
    }

    /**
     * Has active hostiles
     *
     * @return boolean
     */
    boolean hasActiveHostiles()
    {
        return getActiveHostiles().size() > 0;
    }

    /**
     * Is this a null roster
     *
     * @return boolean
     */
    boolean isNull()
    {
        return false;
    }

    /**
     * Remove hostile
     *
     * @param hostile Hostile to remove
     *
     * @throws CustomException If hostile is not in roster
     */
    void remove(@NotNull EncounteredHostile hostile) throws CustomException
    {
        if (!containsHostile(hostile.getName())) {
            throw new CustomException(String.format(
                "I don't see any hostiles named %s in this encounter",
                hostile.getName()
            ));
        }

        hostileRoster.remove(hostile);
    }

    /**
     * Assert that no hostiles have this nickname
     *
     * @param nickname Nickname to check for
     */
    private void assertUniqueNickname(@NotNull String nickname)
    {
        String nicknameToLower = nickname.toLowerCase();
        for (EncounteredHostile hostile : hostileRoster) {
            if (hostile.getName().toLowerCase().equals(nicknameToLower)) {
                throw new CustomException(String.format("There's already a hostile named %s in this battle", nickname));
            }
        }
    }

    /**
     * Is hostile with given name currently in the roster
     *
     * @param name Hostile name
     *
     * @return bool
     */
    private boolean containsHostile(@NotNull String name)
    {
        for (EncounteredHostile hostile : hostileRoster) {
            if (hostile.isName(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all hostiles of the given species that don't have a nickname
     *
     * @param species Species to filter hostiles by
     *
     * @return ArrayList
     */
    private ArrayList<EncounteredHostile> getHostilesWithoutNicknames(@NotNull String species)
    {
        ArrayList<EncounteredHostile> hostiles = new ArrayList<>();
        for (EncounteredHostile hostile : hostileRoster) {
            if (hostile.getSpecies().equals(species)) {
                hostiles.add(hostile);
            }
        }

        return hostiles;
    }

    /**
     * Handle all hostiles of the same species that don't have nicknames
     *
     * @param newHostile New hostile to add
     */
    private void handleSameSpecies(@NotNull EncounteredHostile newHostile)
    {
        String species      = newHostile.getSpecies();
        int    speciesCount = 0;
        for (EncounteredHostile hostile : getHostilesWithoutNicknames(species)) {
            char letter = (char) (BASE_CHAR + speciesCount++);
            hostile.setName(species + letter);
        }

        char letter = (char) (BASE_CHAR + speciesCount);
        newHostile.setName(species + letter);
    }
}
