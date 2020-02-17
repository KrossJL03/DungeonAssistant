package bot.Battle.HostileEncounter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class HostileRoster implements HostileRosterInterface
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
     * {@inheritDoc}
     */
    public void addHostile(@NotNull EncounteredHostile newHostile)
    {
        if (newHostile.hasNickname()) {
            assertUniqueNickname(newHostile.getName());
        } else {
            handleSameSpecies(newHostile);
        }

        hostileRoster.add(newHostile);
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull ArrayList<EncounteredHostile> getActiveHostiles()
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
     * {@inheritDoc}
     */
    public @NotNull ArrayList<EncounteredHostile> getAllHostiles()
    {
        return new ArrayList<>(hostileRoster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredHostile getHostile(@NotNull String name) throws HostileRosterException
    {
        for (EncounteredHostile hostile : hostileRoster) {
            if (hostile.isName(name)) {
                return hostile;
            }
        }

        throw HostileRosterException.createHostileNotFound(name);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasActiveHostiles()
    {
        return getActiveHostiles().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNull()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(@NotNull EncounteredHostile hostile) throws HostileRosterException
    {
        if (!containsHostile(hostile.getName())) {
            throw HostileRosterException.createHostileNotFound(hostile.getName());
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
                throw HostileRosterException.createNicknameInUse(nickname);
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
