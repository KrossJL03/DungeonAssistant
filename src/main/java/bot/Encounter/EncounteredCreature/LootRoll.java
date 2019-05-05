package bot.Encounter.EncounteredCreature;

import bot.Encounter.LootRollInterface;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public class LootRoll implements LootRollInterface
{
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
    @NotNull LootRoll(int roll, @NotNull String hostileName, @NotNull Loot loot) {
        this.hostileName = hostileName;
        this.loot = loot;
        this.roll = roll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getKillName() {
        return hostileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Loot getLoot() {
        return loot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLootRoll() {
        return roll;
    }
}
