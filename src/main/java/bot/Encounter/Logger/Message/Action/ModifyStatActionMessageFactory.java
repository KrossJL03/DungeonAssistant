package bot.Encounter.Logger.Message.Action;

import bot.Constant;
import bot.Encounter.ModifyStatActionResultInterface;
import bot.MessageInterface;
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
            "%s's %s has gone %s by %d, making it %s!",
            result.getCreatureName(),
            result.getStatName(),
            result.getStatMod() > 0 ? "up" : "down",
            result.getStatMod(),
            result.isHitpointStat() && result.getStatValue() > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS
            ? "???"
            : result.getStatValue()
        ));

        return message;
    }
}
