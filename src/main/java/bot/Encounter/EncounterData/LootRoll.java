package bot.Encounter.EncounterData;

import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public class LootRoll {

    private Loot   loot;
    private String hostileName;
    private int    roll;

    /**
     * LootRoll constructor
     *
     * @param roll        Roll
     * @param hostileName Hostile name
     * @param loot        loot
     */
    LootRoll(int roll, String hostileName, Loot loot) {
        this.hostileName = hostileName;
        this.loot = loot;
        this.roll = roll;
    }

    /**
     * Get hostile name
     *
     * @return String
     */
    @NotNull
    public String getHostileName() {
        return this.hostileName;
    }

    /**
     * Get loot
     *
     * @return Loot
     */
    @NotNull
    public Loot getLoot() {
        return this.loot;
    }

    /**
     * Get roll
     *
     * @return ing
     */
    public int getRoll() {
        return this.roll;
    }
}
