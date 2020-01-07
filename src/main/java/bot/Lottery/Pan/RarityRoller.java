package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

abstract class RarityRoller
{
    final static protected int RARITY_DIE = 100;

    /**
     * Roll rarity
     *
     * @return Rarity
     */
    abstract @NotNull PanRarity roll();

    /**
     * Roll rarity
     *
     * @return int
     */
    final protected int rollRarity()
    {
        return (int) Math.floor(Math.random() * RARITY_DIE) + 1;
    }
}
