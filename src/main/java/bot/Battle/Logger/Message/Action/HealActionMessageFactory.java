package bot.Battle.Logger.Message.Action;

import bot.Battle.HealActionResult;
import bot.Constant;
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
    public @NotNull MessageInterface createMessage(@NotNull HealActionResult result)
    {
        ActionMessage message   = new ActionMessage();
        int           currentHp = result.getCurrentHp();
        int           maxHp     = result.getMaxHp();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s heals %d points! [%s/%s]",
            result.getName(),
            result.getHealedHp(),
            currentHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : currentHp,
            maxHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : maxHp
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
