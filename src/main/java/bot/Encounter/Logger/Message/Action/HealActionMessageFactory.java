package bot.Encounter.Logger.Message.Action;

import bot.Encounter.HealActionResultInterface;
import bot.Encounter.Logger.Message.MessageInterface;
import org.jetbrains.annotations.NotNull;

class HealActionMessageFactory extends ActionMessageFactory
{
    /**
     * Create message from heal action result
     *
     * @param result Heal action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull HealActionResultInterface result)
    {
        ActionMessage message = new ActionMessage();

        message.add(String.format(
            "%s heals %d points! [%d/%d]",
            result.getName(),
            result.getHealedHp(),
            result.getCurrentHp(),
            result.getMaxHp()
        ));

        return message;
    }
}
