package bot.Encounter.Logger.MessageBuilder;

import org.jetbrains.annotations.NotNull;

public interface DodgeSubActionDataInterface {

    /**
     * Get attacker name
     *
     * @return String
     */
    @NotNull
    String getAttackerName();

    /**
     * Get damage roll
     *
     * @return int
     */
    int getDamageRoll();

    /**
     * Get dodge roll
     *
     * @return int
     */
    int getDodgeRoll();

    /**
     * Is successful dodge
     *
     * @return boolean
     */
    boolean isSuccess();

}
