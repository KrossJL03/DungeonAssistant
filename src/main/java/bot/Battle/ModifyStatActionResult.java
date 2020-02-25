package bot.Battle;

import bot.Constant;
import org.jetbrains.annotations.NotNull;

public class ModifyStatActionResult implements ActionResultInterface
{
    private String creatureName;
    private int    statMod;
    private String statName;
    private int    statValue;

    /**
     * Constructor.
     *
     * @param creatureName Name of creature with modded stat
     * @param statName     Name of modded stat
     * @param statMod      Amount stat was modified
     * @param statValue    Value of stat after modification
     */
    public ModifyStatActionResult(
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
     * Get creature name
     *
     * @return String
     */
    public @NotNull String getCreatureName()
    {
        return creatureName;
    }

    /**
     * Get stat mod
     *
     * @return int
     */
    public int getStatMod()
    {
        return statMod;
    }

    /**
     * Get stat name
     *
     * @return String
     */
    public @NotNull String getStatName()
    {
        return statName;
    }

    /**
     * Get stat value
     *
     * @return int
     */
    public int getStatValue()
    {
        return statValue;
    }

    /**
     * Is the modified stat a hitpoints stat
     *
     * @return boolean
     */
    public boolean isHitpointStat()
    {
        return statName.equals(Constant.HOSTILE_STAT_HITPOINTS) ||
               statName.equals(Constant.EXPLORER_STAT_HITPOINTS_SHORT);
    }
}
