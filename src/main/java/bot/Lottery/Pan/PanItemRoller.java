package bot.Lottery.Pan;

import org.jetbrains.annotations.NotNull;

class PanItemRoller
{
    /**
     * Roll item
     *
     * @param rarity Rarity
     *
     * @return Rarity
     */
    @NotNull PanRollResult roll(@NotNull PanRarity rarity)
    {
        int     itemDie = PanItemRegistry.getItemCountByRarity(rarity);
        PanItem item    = PanItemRegistry.getItemByRarityAndRoll(rarity, rollItemDie(itemDie));

        return new PanRollResult(item, rarity, itemDie);
    }

    /**
     * Roll item by rarity
     *
     * @param itemDie Item die
     *
     * @return int
     */
    private int rollItemDie(int itemDie)
    {
        return (int) Math.floor(Math.random() * itemDie) + 1;
    }
}
