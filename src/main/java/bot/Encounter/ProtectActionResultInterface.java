package bot.Encounter;

import bot.Encounter.Logger.Mention;
import org.jetbrains.annotations.NotNull;

public interface ProtectActionResultInterface extends CombatActionResultInterface
{
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
