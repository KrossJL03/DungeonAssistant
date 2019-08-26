package bot.Lottery.Mofongo;

import org.jetbrains.annotations.NotNull;

class MofongoPetStat
{
    final static String RARITY_PET_COMMON     = "Common";
    final static String RARITY_PET_UNCOMMON   = "Uncommon";
    final static String RARITY_PET_RARE       = "Rare";
    final static String RARITY_PET_ULTRA_RARE = "UltraRare";
    final static String RARITY_PET_LEGENDARY  = "Legendary";

    private String speciesName;
    private String rarityName;
    private int    rollCount;

    /**
     * Constructor.
     *
     * @param speciesName Species name
     * @param rarityName  Rarity name
     * @param rollCount   Roll count
     */
    MofongoPetStat(@NotNull String speciesName, @NotNull String rarityName, int rollCount)
    {
        this.rarityName = rarityName;
        this.rollCount = rollCount;
        this.speciesName = speciesName;
    }

    /**
     * Get rarity name
     *
     * @return String
     */
    @NotNull String getRarityName()
    {
        return rarityName;
    }

    /**
     * Get roll count
     *
     * @return int
     */
    int getRollCount()
    {
        return rollCount;
    }

    /**
     * Get species name
     *
     * @return String
     */
    @NotNull String getSpeciesName()
    {
        return speciesName;
    }
}
