package bot.Encounter;

import org.jetbrains.annotations.NotNull;

public interface ModifyStatActionResultInterface extends ActionResultInterface
{
    /**
     * Get creature name
     *
     * @return String
     */
    @NotNull String getCreatureName();

    /**
     * Get stat name
     *
     * @return String
     */
    @NotNull String getStatName();

    /**
     * Get stat mod
     *
     * @return int
     */
    int getStatMod();

    /**
     * Get stat value
     *
     * @return int
     */
    int getStatValue();
}
