package bot.Battle.EncounteredCreature;

import bot.Battle.ModifyStatActionResultInterface;
import org.jetbrains.annotations.NotNull;

class ModifyStatActionResult implements ModifyStatActionResultInterface
{
    private String creatureName;
    private String statName;
    private int    statMod;
    private int    statValue;

    /**
     * ModifyStatActionResult constructor
     *
     * @param creatureName Name of creature with modded stat
     * @param statName     Name of modded stat
     * @param statMod      Amount stat was modified
     * @param statValue    Value of stat after modification
     */
    @NotNull ModifyStatActionResult(
        @NotNull String creatureName,
        @NotNull String statName,
        int statMod,
        int statValue
    )
    {
        this.creatureName = creatureName;
        this.statMod = statMod;
        this.statName = statName;
        this.statValue = statValue;
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull String getCreatureName()
    {
        return creatureName;
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull String getStatName()
    {
        return statName;
    }

    /**
     * {@inheritDoc}
     */
    public int getStatMod()
    {
        return statMod;
    }

    /**
     * {@inheritDoc}
     */
    public int getStatValue()
    {
        return statValue;
    }
}
