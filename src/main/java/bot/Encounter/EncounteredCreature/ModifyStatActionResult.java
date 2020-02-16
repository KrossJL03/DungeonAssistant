package bot.Encounter.EncounteredCreature;

import bot.Constant;
import bot.Encounter.ModifyStatActionResultInterface;
import org.jetbrains.annotations.NotNull;

class ModifyStatActionResult implements ModifyStatActionResultInterface
{
    private String creatureName;
    private int    statMod;
    private String statName;
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
    @Override
    public @NotNull String getCreatureName()
    {
        return creatureName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatMod()
    {
        return statMod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStatName()
    {
        return statName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatValue()
    {
        return statValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHitpointStat()
    {
        return statName.equals(Constant.HOSTILE_STAT_HITPOINTS) ||
               statName.equals(Constant.EXPLORER_STAT_HITPOINTS_SHORT);
    }
}
