package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

class BasicRarityRoller extends RarityRoller
{
    /**
     * {@inheritDoc}
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
}
