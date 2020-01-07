package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

class SpecialRarityRoller extends RarityRoller
{
    /**
     * {@inheritDoc}
     */
    @NotNull PanRarity roll()
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
}
