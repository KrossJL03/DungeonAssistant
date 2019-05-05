package bot.Encounter.Logger.MessageBuilder;

import bot.Encounter.Logger.Mention;
import org.jetbrains.annotations.NotNull;

public interface ProtectActionDataInterface extends CombatActionDataInterface {

    /**
     * Get name of protected explorer
     *
     * @return String
     */
    @NotNull String getProtectedName();

    /**
     * Get mention for protected explorer's owner
     *
     * @return String
     */
    @NotNull Mention getProtectedOwnerMention();
}
