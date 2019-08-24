package bot.Hostile;

import bot.Hostile.Exception.HostileNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class HostileManager
{

    public HostileManager() {}

    /**
     * Create hostile
     *
     * @param species       Species
     * @param dangerLevel   Danger level
     * @param hitpoints     Hitpoints
     * @param attackDie     Attack die rolled for attacks
     * @param attackCount   Number of attacks per round
     * @param lootRollCount Number of loot die rolled
     * @param isViewable    Is viewable
     */
    static void createHostile(
        String species,
        int dangerLevel,
        int hitpoints,
        int attackDie,
        int attackCount,
        int lootRollCount,
        boolean isViewable
    )
    {
        Hostile hostile = new Hostile(
            species,
            dangerLevel,
            hitpoints,
            attackDie,
            attackCount,
            lootRollCount,
            new HashMap<>(),
            isViewable
        );
        HostileRepository.insertHostile(hostile);
    }

    /**
     * Get hostile by species
     *
     * @param species Species of hostile to retrieve
     *
     * @return Hostile
     */
    public static @NotNull Hostile getHostile(@NotNull String species)
    {
        return HostileRepository.getHostile(species);
    }

    /**
     * Set loot for hostile
     *
     * @param hostileSpecies Hostile species to set loot for
     * @param diceRoll       Loot die roll
     * @param itemName       Loot item name
     * @param quantity       Quantity of item
     */
    static void setLoot(@NotNull String hostileSpecies, int diceRoll, @Nullable String itemName, int quantity)
    {
        int hostileId = HostileRepository.getHostileId(hostileSpecies);
        if (hostileId < 0) {
            throw HostileNotFoundException.createNotInDatabase(hostileSpecies);
        }
        LootRepository.insertLoot(hostileId, diceRoll, itemName, quantity);
    }

}
