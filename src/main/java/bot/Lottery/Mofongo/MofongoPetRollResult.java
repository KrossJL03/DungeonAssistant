package bot.Lottery.Mofongo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class MofongoPetRollResult
{
    private ArrayList<MofongoItem> items;
    private int                    itemDie;

    /**
     * Constructor.
     *
     * @param items   Items rolled
     * @param itemDie Item die
     */
    MofongoPetRollResult(@NotNull ArrayList<MofongoItem> items, int itemDie)
    {
        this.itemDie = itemDie;
        this.items = items;
    }

    /**
     * Get item die
     *
     * @return int
     */
    int getItemDie()
    {
        return itemDie;
    }

    /**
     * Get items
     *
     * @return ArrayList
     */
    @NotNull ArrayList<MofongoItem> getItems()
    {
        return items;
    }
}
