package bot.Battle.Logger.Message.Action;

import bot.MessageInterface;
import bot.Battle.ModifyStatActionResultInterface;
import org.jetbrains.annotations.NotNull;

class ModifyStatActionMessageFactory extends ActionMessageFactory
{
    /**
     * Create message from modify stat action result
     *
     * @param result Modify stat action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull ModifyStatActionResultInterface result)
    {
        ActionMessage message = new ActionMessage();

        message.add(String.format(
            "%s's %s has gone %s by %d, making it %d!",
            result.getCreatureName(),
            result.getStatName(),
            result.getStatMod() > 0 ? "up" : "down",
            result.getStatMod(),
            result.getStatValue()
        ));

        return message;
    }
}
