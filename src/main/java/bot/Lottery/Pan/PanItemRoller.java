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
        int     itemDie  = PanItemRegistry.getItemCountByRarity(rarity);
        int     itemRoll = rollItemDie(itemDie);
        String  itemName = PanItemRegistry.getItemNameByRarityAndRoll(rarity, itemRoll);
        PanItem item     = new PanItem(itemName, itemDie, itemRoll);

        return new PanRollResult(item, rarity);
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
