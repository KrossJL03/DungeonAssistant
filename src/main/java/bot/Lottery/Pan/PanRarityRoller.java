package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

class PanRarityRoller
{
    final static private int RARITY_DIE = 100;

    /**
     * Roll rarity
     *
     * @return Rarity
     */
    @NotNull PanRarity roll()
    {
        int    roll = rollRarity();
        String name;

        if (roll > 90) {
            name = PanRarity.RARITY_NAME_ULTRA_RARE;
        } else if (roll > 75) {
            name = PanRarity.RARITY_NAME_RARE;
        } else if (roll > 45) {
            name = PanRarity.RARITY_NAME_UNCOMMON;
        } else {
            name = PanRarity.RARITY_NAME_COMMON;
        }

        return new PanRarity(name, RARITY_DIE, roll);
    }

    /**
     * Roll special rarity
     *
     * @return Rarity
     */
    @NotNull PanRarity rollSpecial()
    {
        int    roll = rollRarity();
        String name;

        if (roll > 95) {
            name = PanRarity.RARITY_NAME_SPECIAL;
        } else if (roll > 85) {
            name = PanRarity.RARITY_NAME_ULTRA_RARE;
        } else if (roll > 70) {
            name = PanRarity.RARITY_NAME_RARE;
        } else if (roll > 40) {
            name = PanRarity.RARITY_NAME_UNCOMMON;
        } else {
            name = PanRarity.RARITY_NAME_COMMON;
        }

        return new PanRarity(name, RARITY_DIE, roll);
    }

    /**
     * Roll rarity
     *
     * @return int
     */
    private int rollRarity()
    {
        return (int) Math.floor(Math.random() * RARITY_DIE) + 1;
    }
}
