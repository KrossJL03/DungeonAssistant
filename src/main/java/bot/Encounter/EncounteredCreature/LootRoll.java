package bot.Encounter.EncounteredCreature;

import bot.Encounter.LootRollInterface;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public class LootRoll implements LootRollInterface
{
    private Loot   loot;
    private String hostileName;
    private int    lootDie;
    private int    lootRoll;

    /**
     * LootRoll constructor
     *
     * @param hostileName Hostile name
     * @param loot        Loot
     * @param lootDie     Loot die
     * @param lootRoll    Loot roll
     */
    public @NotNull LootRoll(@NotNull String hostileName, @NotNull Loot loot, int lootDie, int lootRoll)
    {
        this.hostileName = hostileName;
        this.loot = loot;
        this.lootDie = lootDie;
        this.lootRoll = lootRoll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getKillName()
    {
        return hostileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Loot getLoot()
    {
        return loot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLootDie()
    {
        return lootDie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLootRoll()
    {
        return lootRoll;
    }
}
