package bot.Battle.HostileEncounter;

import bot.Battle.EncounteredHostileInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class HostileRoster implements HostileRosterInterface
{
    private static int                                    BASE_CHAR = 65;
    private        ArrayList<EncounteredHostileInterface> hostileRoster;

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
    public void addHostile(@NotNull EncounteredHostileInterface newHostile)
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
    public @NotNull ArrayList<EncounteredHostileInterface> getActiveHostiles()
    {
        ArrayList<EncounteredHostileInterface> activeHostiles = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : hostileRoster) {
            if (!encounteredHostile.isSlain()) {
                activeHostiles.add(encounteredHostile);
            }
        }

        return activeHostiles;
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull ArrayList<EncounteredHostileInterface> getAllHostiles()
    {
        return new ArrayList<>(hostileRoster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EncounteredHostileInterface getHostile(@NotNull String name) throws HostileRosterException
    {
        for (EncounteredHostileInterface encounteredHostile : hostileRoster) {
            if (encounteredHostile.isName(name)) {
                return encounteredHostile;
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
    public void remove(@NotNull EncounteredHostileInterface hostile) throws HostileRosterException
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
        for (EncounteredHostileInterface hostile : hostileRoster) {
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
        for (EncounteredHostileInterface hostile : hostileRoster) {
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
    private ArrayList<EncounteredHostileInterface> getHostilesWithoutNicknames(@NotNull String species)
    {
        ArrayList<EncounteredHostileInterface> hostiles = new ArrayList<>();
        for (EncounteredHostileInterface hostile : hostileRoster) {
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
    private void handleSameSpecies(@NotNull EncounteredHostileInterface newHostile)
    {
        String species      = newHostile.getSpecies();
        int    speciesCount = 0;
        for (EncounteredHostileInterface hostile : getHostilesWithoutNicknames(species)) {
            char letter = (char) (BASE_CHAR + speciesCount++);
            hostile.setName(species + letter);
        }

        char letter = (char) (BASE_CHAR + speciesCount);
        newHostile.setName(species + letter);
    }
}
