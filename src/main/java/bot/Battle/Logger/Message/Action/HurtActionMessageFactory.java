package bot.Battle.Logger.Message.Action;

import bot.Battle.HurtActionResultInterface;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class HurtActionMessageFactory extends ActionMessageFactory
{
    /**
     * Create message from hurt action result
     *
     * @param result Hurt action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull HurtActionResultInterface result)
    {
        ActionMessage message = new ActionMessage();

        message.add(String.format(
            "%s takes %d dmg! [%d/%d]",
            result.getName(),
            result.getHurtHp(),
            result.getCurrentHp(),
            result.getMaxHp()
        ));
        if (result.isSlain()) {
            message.add(String.format("%s was slain", result.getName()));
        }

        return message;
    }
}
