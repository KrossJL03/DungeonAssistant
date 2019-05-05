package bot.Encounter.Logger.MessageBuilder;

import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public interface LootSubActionDataInterface {

    /**
     * Get kill name
     *
     * @return String
     */
    @NotNull
    String getKillName();

    /**
     * Get loot
     *
     * @return Loot
     */
    @NotNull
    Loot getLoot();

    /**
     * Get loot roll
     *
     * @return int
     */
    int getLootRoll();
}
