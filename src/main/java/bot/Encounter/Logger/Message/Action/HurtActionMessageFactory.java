package bot.Encounter.Logger.Message.Action;

import bot.Constant;
import bot.Encounter.HurtActionResultInterface;
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
        ActionMessage message   = new ActionMessage();
        int           currentHp = result.getCurrentHp();
        int           maxHp     = result.getMaxHp();

        message.add(String.format(
            "%s takes %d dmg! [%s/%s]",
            result.getName(),
            result.getHealedHp(),
            currentHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : currentHp,
            maxHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : maxHp
        ));

        if (result.isSlain()) {
            message.add(String.format("%s was slain", result.getName()));
        }

        return message;
    }
}
