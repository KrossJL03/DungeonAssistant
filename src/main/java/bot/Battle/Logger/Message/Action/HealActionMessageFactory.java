package bot.Battle.Logger.Message.Action;

import bot.Battle.HealActionResultInterface;
import bot.MessageInterface;
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

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s heals %d points! [%d/%d]",
            result.getName(),
            result.getHealedHp(),
            result.getCurrentHp(),
            result.getMaxHp()
        ));
        if (result.wasTargetRevived()) {
            message.add(String.format(
                "%s has been revived! They have earned the %s title.",
                result.getName(),
                codeFormatter.makeCyan("Zombie")
            ));
        }
        message.endCodeBlock();

        return message;
    }
}
